package com.pdm.cher.firebase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.pdm.cher.data.Invite
import com.pdm.cher.data.Game
import com.pdm.cher.data.Player
import com.pdm.cher.hash256
import kotlinx.coroutines.tasks.await


class ReversiFirebase(db: FirebaseFirestore, private val auth: FirebaseAuth, private val storageRef: FirebaseStorage) {
    private val lobbyCollection = db.collection("lobby")
    private val inviteCollection = db.collection("paringAwaiting")
    private val gamesCollection = db.collection("games")
    private val playersCollection = db.collection("players")

    suspend fun enterLobby(player: Player) {
        val playerInLobby = lobbyCollection.document(player.email).get().await().toObject(Player::class.java)
        if(playerInLobby != null) throw FirebaseExceptions.PlayerAlreadyInLobby()
        lobbyCollection.document(player.email).set(player).await()
    }

    suspend fun getLobby(): List<Player> = lobbyCollection.get().await().documents.map {
        it.toObject(Player::class.java)!!
    }

    suspend fun leaveLobby(player: Player){
        lobbyCollection.document(player.email).delete().await()
        inviteCollection.document(player.email).delete().await()
        inviteCollection.whereEqualTo("playerInvitingEmail", player.email).get().await().documents.forEach {
            it.reference.delete().await()
        }
    }

    suspend fun invitePlayer(invitingPlayer: Player, invitedPlayer: Player): Invite{
        val invitingPlayerInLobby = lobbyCollection.document(invitedPlayer.email).get().await().toObject(Player::class.java)
        val invitingPlayerInvite = inviteCollection.document(invitingPlayer.email).get().await().toObject(Invite::class.java)
        val invitedPlayerInLobby = lobbyCollection.document(invitedPlayer.email).get().await().toObject(Player::class.java)
        val invitedPlayerInvite = inviteCollection.document(invitedPlayer.email).get().await().toObject(Invite::class.java)

        if(invitingPlayerInvite != null) throw FirebaseExceptions.PlayerAlreadyInvited()
        if(invitedPlayerInvite != null) throw FirebaseExceptions.AlreadyInvitedAPlayer()
        if(invitingPlayerInLobby == null) throw FirebaseExceptions.PlayerNoLongerAvailable()
        if(invitedPlayerInLobby == null) throw FirebaseExceptions.PlayerNoLongerAvailable()
        val invite = Invite(invitingPlayer.email)
        inviteCollection.document(invitedPlayer.email).set(invite).await()
        return invite
    }

    suspend fun getInvite(invitedPlayer: Player): Invite =
        inviteCollection.document(invitedPlayer.email).get().await().toObject(Invite::class.java) ?: throw FirebaseExceptions.InviteNoLongerAvailable()


    suspend fun acceptInvite(currentPlayer: Player){
        val invite = inviteCollection.document(currentPlayer.email).get().await().toObject(Invite::class.java) ?: throw FirebaseExceptions.PlayerNoLongerAvailable()
        inviteCollection.document(currentPlayer.email).set(invite.copy(accepted = true)).await()
    }

    suspend fun declineInvite(currentPlayer: Player){
        inviteCollection.document(currentPlayer.email).delete().await()
    }

    suspend fun startGame(invitingPlayer: Player, invitedPlayer: Player): String{
        lobbyCollection.document(invitingPlayer.email).delete().await()
        lobbyCollection.document(invitedPlayer.email).delete().await()
        inviteCollection.document(invitedPlayer.email).delete().await()
        val random = (Math.random() * 100).toInt()
        val newGame = if(random < 50){
            Game(invitingPlayer.email, invitedPlayer.email)
        } else {
            Game(invitedPlayer.email, invitingPlayer.email)
        }
        gamesCollection.document(newGame.id).set(newGame).await()
        return newGame.id
    }

    suspend fun getGameById(gameId: String): Game? = gamesCollection.document(gameId).get().await().toObject(Game::class.java)

    suspend fun getGameIdByPlayerEmail(playerEmail: String): String =
        gamesCollection.whereEqualTo("playerBlackEmail", playerEmail).get().await().documents.map {
            it.toObject(Game::class.java)?.id
        }.firstOrNull() ?: gamesCollection.whereEqualTo("playerWhiteEmail", playerEmail).get().await().documents.map {
            it.toObject(Game::class.java)?.id
        }.firstOrNull() ?: throw FirebaseExceptions.PlayerNotInGame()

    suspend fun endGame(game: Game){
        gamesCollection.document(game.id).delete().await()
    }

    suspend fun makePlay(game:Game){
        gamesCollection.document(game.id).set(game).await()
    }

    suspend fun uploadPlayerImage(player: Player, image: ByteArray): ImageBitmap{
        val imageRef = storageRef.reference.child("images/${player.email}.jpg")
        imageRef.putBytes(image).await()
        val url = imageRef.downloadUrl.await().toString()
        playersCollection.document(player.email).set(player.copy(imageURL = url)).await()
        return BitmapFactory.decodeByteArray(image, 0, image.size)?.let {originalBitmap ->
            Bitmap.createScaledBitmap(originalBitmap, 1024, 1024, true).asImageBitmap()
        } ?: throw FirebaseExceptions.ImageCouldNotBeLoaded()
    }

    suspend fun getPlayerImage(email: String): ImageBitmap {
        val imageRef = storageRef.reference.child("images/$email.jpg")
        val url = imageRef.downloadUrl.await().toString()
        return Firebase.storage.getReferenceFromUrl(url).getBytes(1024 * 1024).await().let {
            val originalBitmap = BitmapFactory.decodeByteArray(it, 0, it.size) ?: throw FirebaseExceptions.ImageCouldNotBeLoaded()
            Bitmap.createScaledBitmap(originalBitmap, 1024, 1024, true).asImageBitmap()
        }
    }

    suspend fun register(username: String, email: String, password: String): Player {
        if(!email.matches(Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}\$")))
            throw FirebaseExceptions.InvalidEmail()
        if(!password.matches(Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")))
            throw FirebaseExceptions.InvalidPassword()
        val authResult = auth.createUserWithEmailAndPassword(email, password.hash256()).await()
        authResult.user?.let {
            val newPlayer = Player(username, email)
            playersCollection.document(email).set(newPlayer).await()
            return newPlayer
        }
        throw FirebaseExceptions.FailedToRegister()
    }

    suspend fun login(email: String, password: String): Player {
        val authResult = auth.signInWithEmailAndPassword(email, password.hash256()).await()
        authResult.user ?: throw FirebaseExceptions.InvalidCredentials()
        return playersCollection.document(email).get().await().toObject(Player::class.java)!!
    }

    suspend fun getPlayer(email: String): Player? = playersCollection.document(email).get().await().toObject(Player::class.java)

}