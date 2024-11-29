package com.pdm.cher.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.pdm.cher.data.Invite
import com.pdm.cher.data.Game
import com.pdm.cher.data.Player
import com.pdm.cher.reversi.Square
import kotlinx.coroutines.tasks.await


class ReversiFirebase(private val db: FirebaseFirestore) {
    private val lobbyCollection = db.collection("lobby")
    private val awaitingCollection = db.collection("paringAwaiting")
    private val gamesCollection = db.collection("games")
    private val playersCollection = db.collection("players")

    suspend fun enterLobby(player: Player): List<Player>{
        return try{
            lobbyCollection.add(player).await()
            val lobbyList: List<Player> = lobbyCollection.get().await().documents.map {
                println(it.data)
                it.toObject(Player::class.java) ?: throw NullPointerException()
            }.filter { it.id != player.id }
            lobbyList
        } catch (e: NullPointerException){
            throw FirebaseExceptions.FailedToConvertToObject()
        } catch (e: Exception){
            Log.e("ReversiFirebase", e.toString())
            throw FirebaseExceptions.FailedToEnterLobby()
        }


    }

    suspend fun invitePlayer(invitingPlayer: Player, invitedPlayer: Player){
        try {
            val lobbyList: List<Player> = lobbyCollection.get().await().documents.map {
                it.toObject(Player::class.java) ?: throw NullPointerException()
            }
            if (lobbyList.isEmpty() || lobbyList.size == 1) throw NullPointerException()
            if (!lobbyList.contains(invitedPlayer)) throw NullPointerException()
            val paringAwaitingCollection = db.collection("paringAwaiting")
            paringAwaitingCollection.add(Invite(invitingPlayer.id,invitedPlayer.id)).await()
        }  catch (_: NullPointerException){
            throw FirebaseExceptions.FailedToConvertToObject()
        } catch (_: Exception){
            throw FirebaseExceptions.FailedToPickPlayer()
        }
    }

    suspend fun checkInvites(invitedPlayer: Player): List<Invite> {
        try {
            return awaitingCollection.get().await().documents.map {
                it.toObject(Invite::class.java) ?: throw NullPointerException()
            }.filter { it.playerInvitedId == invitedPlayer.id }
        } catch (_: NullPointerException){
            throw FirebaseExceptions.FailedToConvertToObject()
        } catch (_: Exception){
            throw FirebaseExceptions.FailedToCheckParing()
        }
    }

    suspend fun acceptedPairing(invitingPlayer: Player, invitedPlayer: Player): Boolean?{
        return try {
            awaitingCollection.get().await().documents.map {
                it.toObject(Invite::class.java) ?: throw NullPointerException()
            }.firstOrNull{ it.invitingPlayer == invitingPlayer.id && it.playerInvitedId == invitedPlayer.id }?.accepted
        } catch (_: NullPointerException){
            throw FirebaseExceptions.FailedToConvertToObject()
        } catch (_: Exception){
            throw FirebaseExceptions.FailedToCheckParing()
        }
    }

    suspend fun acceptInvite(invitation: Invite){
        try {
            val documentId = awaitingCollection.whereEqualTo("invitingPlayer", invitation.invitingPlayer)
                .whereEqualTo("playerInvitedId", invitation.playerInvitedId).get().await().documents.firstOrNull()?.id
            if(documentId != null) awaitingCollection.document(documentId).delete().await()
            awaitingCollection.document().set(invitation.copy(accepted = true)).await()
        } catch (_: Exception){
            throw FirebaseExceptions.FailedToCheckParing()
        }
    }

    suspend fun startGame(playerOneId: Int, playerTwoId: Int): Game{
        try{
            lobbyCollection.whereEqualTo("id", playerOneId).get().await().documents.forEach {
                it.reference.delete()
            }
            lobbyCollection.whereEqualTo("id", playerTwoId).get().await().documents.forEach {
                it.reference.delete()
            }
            awaitingCollection.whereEqualTo("invitingPlayer", playerOneId).get().await().documents.forEach {
                it.reference.delete()
            }
            awaitingCollection.whereEqualTo("invitedPlayer", playerOneId).get().await().documents.forEach {
                it.reference.delete()
            }
            awaitingCollection.whereEqualTo("inviting", playerTwoId).get().await().documents.forEach {
                it.reference.delete()
            }
            awaitingCollection.whereEqualTo("invited", playerTwoId).get().await().documents.forEach {
                it.reference.delete()
            }
            val random = (Math.random() * 100).toInt()
            val newGame = if(random < 50){
                Game(playerOneId, playerTwoId)
            } else {
                Game(playerTwoId, playerOneId)
            }
            gamesCollection.add(newGame).await()
            return newGame
        } catch (_: Exception){
            throw FirebaseExceptions.FailedToStartGame()
        }
    }

    suspend fun getGame(playerId: Int): Game{
        return try{
            gamesCollection.whereEqualTo("playerOneId", playerId).get().await().documents.map {
                it.toObject(Game::class.java) ?: throw NullPointerException()
            }.firstOrNull() ?: gamesCollection.whereEqualTo("playerTwoId", playerId).get().await().documents.map {
                it.toObject(Game::class.java) ?: throw NullPointerException()
            }.firstOrNull() ?: throw NullPointerException()
        } catch (_: NullPointerException){
            throw FirebaseExceptions.FailedToConvertToObject()
        } catch (_: Exception){
            throw FirebaseExceptions.FailedToGetGame()
        }

    }

    suspend fun makePlay(game: Game, square: Square): Game{
        try {
            val newGame = game.copy(reversi = game.reversi.makePlay(square))
                gamesCollection.document(newGame.toString()).set(newGame).await()
            return newGame
        } catch (_: Exception) {
            throw FirebaseExceptions.FailedToMakePlay()
        }
    }

    suspend fun createPlayer(username: String): Player {


        return try {
            val documents = playersCollection.get().await()
            val highestId = documents.documents
                .mapNotNull { it.id.toIntOrNull() }
                .maxOrNull() ?: 0

            val newId = highestId + 1
            val newPlayer = Player(username, newId)

            playersCollection.document(newId.toString()).set(newPlayer).await()
            newPlayer
        } catch (_: Exception) {
            throw FirebaseExceptions.FailedToCreatePlayer()
        }
    }
}