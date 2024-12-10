package com.pdm.cher.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.pdm.cher.ui.theme.CherTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pdm.cher.screen.GameScreen
import com.pdm.cher.data.Game
import com.pdm.cher.data.Player
import com.pdm.cher.reversi.Color
import com.pdm.cher.serialize
import com.pdm.cher.viewmodels.FirebaseResult
import com.pdm.cher.viewmodels.GameViewModel
import java.time.LocalDateTime


class GameActivity: ComponentActivity() {
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val storage = FirebaseStorage.getInstance()
    private val gameViewModel = GameViewModel(firestore, auth, storage)
    private val onlineGame = mutableStateOf(Game())
    private val localGame = mutableStateOf(Game())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        val gameId = intent.getStringExtra("gameId") ?: ""
        val currentPlayer = intent.getParcelableExtra<Player>("currentPlayer")
        val opponentPlayer = intent.getParcelableExtra<Player>("opponentPlayer")
        onlineGame.value = Game(id = gameId)
        localGame.value = Game(id = gameId)
        if (currentPlayer == null || opponentPlayer == null) {
            finish()
            return // Return because otherwise null-safety complains and I don't have a complaint book
        }
        setContent{
            CherTheme {
                GameScreen(
                    currentPlayer,
                    opponentPlayer,
                    onlineGame,
                    localGame,
                    ::refreshGame,
                    ::makePlay,
                    ::surrender,
                    ::saveGame,
                    ::endGame,
                    ::finish)
            }
        }
    }

    private fun endGame(game: Game){
        gameViewModel.endGame(game){
            if(it is FirebaseResult.Error){
                Log.e("GameActivity", it.message ?: "Error ending game")
            }
        }
    }

    private fun saveGame(save: Boolean, currentPlayer: Player, game: Game){
        if(save){
            val date = LocalDateTime.now().toString().split(".")[0]
            val opponent = if(game.playerBlackEmail == currentPlayer.email) game.playerWhiteEmail else game.playerBlackEmail
            val color = if(game.playerBlackEmail == opponent) "black" else "white"
            this.openFileOutput("${game.id}-$date-Game.txt", MODE_PRIVATE).bufferedWriter().use { out ->
                out.write(color + " " + opponent + " " + game.reversi.playsOrder.serialize())
            }
        }
    }

    private fun surrender(game: Game, color: Color){
        gameViewModel.surrender(game, color){
            if(it is FirebaseResult.Error){
                Log.e("GameActivity", it.message ?: "Error surrendering game")
            }
        }
    }

    private fun refreshGame(onlineGame: MutableState<Game>, localGame: MutableState<Game>, color: Color){
        gameViewModel.getGameById(onlineGame.value.id){
            if(it is FirebaseResult.Success){
                onlineGame.value = it.data
                if(localGame.value.reversi.playsOrder.count() <= it.data.reversi.playsOrder.count()){
                    localGame.value = onlineGame.value
                }
                if(it.data.reversi.getValidPlaySquares(color, it.data.reversi.board).isEmpty()){
                    gameViewModel.skipTurn(it.data){ skip ->
                        if(skip is FirebaseResult.Error){
                            Log.e("GameActivity", skip.message ?: "Error skipping turn")
                        }
                    }
                }
            }
            else if(it is FirebaseResult.Error){
                Log.e("GameActivity", it.message ?: "Error refreshing game")
            }
        }
    }

    private fun makePlay(game: Game, color: Color, row: Int, col: Int){
        gameViewModel.makePlay(game, color, row, col){
            if(it is FirebaseResult.Error){
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}