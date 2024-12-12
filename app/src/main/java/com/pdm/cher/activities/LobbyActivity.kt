package com.pdm.cher.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import com.pdm.cher.data.Invite
import com.pdm.cher.ui.theme.CherTheme
import com.pdm.cher.screen.LobbyScreen
import com.pdm.cher.data.Player
import com.pdm.cher.viewmodels.FirebaseResult
import com.pdm.cher.viewmodels.GameViewModel
import com.pdm.cher.viewmodels.LobbyViewModel
import com.pdm.cher.viewmodels.PlayerInformationViewModel

class LobbyActivity: ComponentActivity() {
    private val lobbyViewModel = LobbyViewModel()
    private val playerInformationViewModel = PlayerInformationViewModel()
    private val gameViewModel = GameViewModel()

    private val invitingPlayer = mutableStateOf<Player?>(null)
    private val lobbyPlayers = mutableStateOf(listOf<Player>())
    private val receivedInvite = mutableStateOf<Invite?>(null)
    private val invitedPlayer = mutableStateOf<Player?>(null)
    private val gameStarted = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        val currentPlayer = intent.getParcelableExtra<Player?>("player")
        if (currentPlayer == null) {
            finish()
            return // Return because otherwise null-safety complains and I don't have a complaint book
        }
        actionBar?.hide()
        super.onCreate(savedInstanceState)
        enterLobby(currentPlayer)
        setContent {
            CherTheme {
                LobbyScreen(
                    currentPlayer,
                    invitingPlayer,
                    invitedPlayer,
                    lobbyPlayers,
                    receivedInvite,
                    gameStarted,
                    ::retrieveOnGoingGame,
                    ::checkSentInvite,
                    ::refreshLobby,
                    ::refreshReceivedInvite,
                    ::retrievePlayerImage,
                    ::sendInvite,
                    ::acceptInvite,
                    ::declineInvite
                )
            }
        }
    }
    private fun enterLobby(player: Player){
        lobbyViewModel.enterLobby(player) {
            when(it) {
                is FirebaseResult.Success -> {
                    lobbyPlayers.value = it.data
                }
                is FirebaseResult.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun getInvitePlayer(email: String, player: MutableState<Player?>){
        playerInformationViewModel.getPlayer(email) {
            when(it) {
                is FirebaseResult.Success -> {
                    player.value = it.data
                }
                is FirebaseResult.Error -> {
                    Log.e("LobbyActivity", it.message ?: "Error retrieving player")
                }
            }
        }
    }

    private fun retrieveOnGoingGame(player: Player){
        lobbyViewModel.getGameId(player) {
            when(it) {
                is FirebaseResult.Success -> {
                    gameViewModel.getGameById(it.data) { game ->
                        when(game) {
                            is FirebaseResult.Success -> {
                                val opponentEmail = if(game.data.playerBlackEmail == player.email) game.data.playerWhiteEmail else game.data.playerBlackEmail
                                playerInformationViewModel.getPlayer(opponentEmail){ opponent ->
                                    when(opponent) {
                                        is FirebaseResult.Success -> {
                                            finish()
                                            this.startActivityIfNeeded(Intent(this, GameActivity::class.java)
                                                .putExtra("gameId", game.data.id)
                                                .putExtra("currentPlayer", player)
                                                .putExtra("opponentPlayer", opponent.data),
                                                0
                                            )
                                            gameStarted.value = true
                                        }
                                        is FirebaseResult.Error -> {
                                            Log.e("LobbyActivity", opponent.message ?: "Error retrieving opponent")
                                        }
                                    }
                                }
                            }
                            is FirebaseResult.Error -> {
                                Log.e("LobbyActivity", game.message ?: "Error retrieving game")
                            }
                        }
                    }
                }
                is FirebaseResult.Error -> {
                    Log.e("LobbyActivity", it.message ?: "Error retrieving game ID")
                }
            }
        }
    }

    private fun startGame(player: Player, opponent: Player){
        lobbyViewModel.startGame(player, opponent) {
            when(it) {
                is FirebaseResult.Success -> {
                    retrieveOnGoingGame(player)
                }
                is FirebaseResult.Error -> {
                    Log.e("LobbyActivity", it.message ?: "Error starting game")
                }
            }
        }
    }

    private fun refreshLobby(){
        lobbyViewModel.getLobby {
            when(it) {
                is FirebaseResult.Success -> {
                    lobbyPlayers.value = it.data
                }
                is FirebaseResult.Error -> {
                    Log.e("LobbyActivity", it.message ?: "Error retrieving lobby players")
                }
            }
        }
    }

    private fun refreshReceivedInvite(currentPlayer: Player){
        lobbyViewModel.getInvite(currentPlayer) {
            when(it) {
                is FirebaseResult.Success -> {
                    receivedInvite.value = it.data
                    getInvitePlayer(it.data.playerInvitingEmail, invitingPlayer)
                }
                is FirebaseResult.Error -> {
                    Log.e("LobbyActivity", it.message ?: "Error retrieving received invite")
                }
            }
        }
    }

    private fun checkSentInvite(currentPlayer: Player, invitedPlayer: Player?){
        if(invitedPlayer != null){
            lobbyViewModel.getInvite(invitedPlayer) {
                when(it) {
                    is FirebaseResult.Success -> {
                        if(it.data.accepted){
                            gameStarted.value = true
                            startGame(currentPlayer, invitedPlayer)
                        }
                    }
                    is FirebaseResult.Error -> {
                        Log.e("LobbyActivity", it.message ?: "Error retrieving invite")
                    }
                }
            }
        }
    }

    fun acceptInvite(currentPlayer: Player){
        lobbyViewModel.acceptInvite(currentPlayer) {
            when(it) {
                is FirebaseResult.Success -> {
                    retrieveOnGoingGame(currentPlayer)
                }
                is FirebaseResult.Error -> {
                    Log.e("LobbyActivity", it.message ?: "Error accepting invite")
                }
            }
        }
    }

    fun declineInvite(currentPlayer: Player){
        lobbyViewModel.declineInvite(currentPlayer) {
            when(it) {
                is FirebaseResult.Success -> {
                    receivedInvite.value = null
                    invitingPlayer.value = null
                }
                is FirebaseResult.Error -> {
                    Log.e("LobbyActivity", it.message ?: "Error declining invite")
                }
            }
        }
    }

    fun sendInvite(currentPlayer: Player, invitedPlayer: Player){
        lobbyViewModel.sendInvite(currentPlayer, invitedPlayer) {
            when(it) {
                is FirebaseResult.Success -> {
                    this.invitedPlayer.value = invitedPlayer
                }
                is FirebaseResult.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun retrievePlayerImage(email: String, playerImage: MutableState<ImageBitmap?>){
        playerInformationViewModel.getPlayerImage(email) {
            when(it) {
                is FirebaseResult.Success -> {
                    playerImage.value = it.data
                }
                is FirebaseResult.Error -> {
                    Log.e("LobbyActivity", it.message ?: "Error retrieving player image")
                }
            }
        }
    }

    override fun onStop() {
        val player = intent.getParcelableExtra<Player?>("player")
        if (player == null) {
            finish()
            return // Return because otherwise null-safety complains and I don't have a complaint book
        }
        super.onStop()
        lobbyViewModel.leaveLobby(player) {
            when(it) {
                is FirebaseResult.Success -> {
                    Log.d("LobbyActivity", "Left lobby")
                }
                is FirebaseResult.Error -> {
                    Log.e("LobbyActivity", it.message ?: "Error leaving lobby")
                }
            }
        }
    }
}