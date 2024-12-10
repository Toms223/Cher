package com.pdm.cher.card

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.pdm.cher.activities.GameActivity
import com.pdm.cher.activities.GameReplayActivity
import com.pdm.cher.data.GameRepetition
import com.pdm.cher.data.Player
import com.pdm.cher.reversi.ReversiRepetition
import java.io.FileInputStream

@Composable
fun FavoriteGamesCard(getAvailableRepetitions: (Array<String>) -> List<String>, currentPlayer: Player) {
    val context = LocalContext.current
    val favoriteGames = remember {
        val files = context.fileList()
        getAvailableRepetitions(files)
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        Text("Favorite Games", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 25.sp, textAlign = TextAlign.Center))

        if (favoriteGames.isEmpty()) {
            Text("No favorite games found", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp, textAlign = TextAlign.Center))
        } else {
            LazyColumn {
                items(favoriteGames) { game ->
                    GameRepetitionButton(game, currentPlayer)
                }
            }
        }
    }
}

@Composable
fun GameRepetitionButton(game:String, currentPlayer: Player) {
    val context = LocalContext.current
    Button(onClick = {
        val filename = game.replace(" ", "T")
        launchGameActivity(filename, currentPlayer, context)
    }) {
        Text(game)
    }
}

fun launchGameActivity(gameName: String, currentPlayer: Player, context: Context) {
    context.startActivity(Intent(context, GameReplayActivity::class.java)
        .putExtra("gameName", gameName)
        .putExtra("player", currentPlayer)
    )
}