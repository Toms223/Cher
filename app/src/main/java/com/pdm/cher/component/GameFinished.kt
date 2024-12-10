package com.pdm.cher.component

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.cher.data.Game
import com.pdm.cher.data.Player
import com.pdm.cher.reversi.GameState
import com.pdm.cher.serialize
import java.time.LocalDateTime

@Composable
fun GameFinished(currentPlayer: Player, game: Game, onExit: () -> Unit, onEndGame: (Game) -> Unit, onSaveGame: (Boolean, Player, Game) -> Unit) {
    val context = LocalContext.current
    val reversi = game.reversi
    var favoriteGame by remember { mutableStateOf(false) }
    if(reversi.state != GameState.PLAYING && reversi.state != GameState.SKIPPED) {
        val gameOverText = @Composable { Text("Game Over", color = MaterialTheme.colorScheme.primary, style = TextStyle(fontWeight = FontWeight.Black, fontSize = 40.sp, textAlign = TextAlign.Center)) }
        when(reversi.state) {
            GameState.FINISHED -> {
                gameOverText()
                Text(
                    "Winner: ${reversi.winner}",
                    color = MaterialTheme.colorScheme.primary,
                    style = TextStyle(
                        fontWeight = FontWeight.Black,
                        fontSize = 40.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
            GameState.SURRENDER -> {
                gameOverText()
                Text(
                    "Winner: ${reversi.winner}",
                    color = MaterialTheme.colorScheme.primary,
                    style = TextStyle(
                        fontWeight = FontWeight.Black,
                        fontSize = 40.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
            GameState.DRAW -> {
                gameOverText()
                Text(
                    "It's a Draw",
                    color = MaterialTheme.colorScheme.primary,
                    style = TextStyle(
                        fontWeight = FontWeight.Black,
                        fontSize = 40.sp,
                        textAlign = TextAlign.Center)
                )
            } else -> {}
        }
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text("Save Game", color = MaterialTheme.colorScheme.primary,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center))
            Spacer(modifier = Modifier.size(8.dp))
            Switch(checked = favoriteGame, onCheckedChange = { favoriteGame = it })
        }
        Button(onClick = {
            onEndGame(game)
            onSaveGame(favoriteGame, currentPlayer, game)
            if(favoriteGame){
                val date = LocalDateTime.now()
                context.openFileOutput("$date-Game.txt", Context.MODE_PRIVATE).use {
                    it.write(reversi.playsOrder.serialize().toByteArray())
                }
            }
            onExit()
        }) {
            Text("Exit Game")
        }
    }
}