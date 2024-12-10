package com.pdm.cher.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.pdm.cher.component.BoardPiece
import com.pdm.cher.component.GameFinished
import com.pdm.cher.component.GamePlayerInfo
import com.pdm.cher.data.Game
import com.pdm.cher.data.Player
import kotlinx.coroutines.delay
import com.pdm.cher.reversi.Color as GameColor



@Preview
@Composable
fun GameScreen(
    currentPlayer: Player,
    opponentPlayer: Player,
    onlineGame: MutableState<Game>,
    localGame: MutableState<Game>,
    onRefreshGame: (MutableState<Game>, MutableState<Game>, GameColor) -> Unit,
    onPlayMade: (Game, GameColor, Int, Int) -> Unit,
    onSurrender: (Game, GameColor) -> Unit,
    onSaveGame: (Boolean, Player, Game) -> Unit,
    onEndGame: (Game) -> Unit,
    onExit: () -> Unit,
) {
    val rememberedLocalGame by remember { localGame }
    val rememberedOnlineGame by remember { onlineGame }
    val color = if (currentPlayer.email == localGame.value.playerBlackEmail) {
        GameColor.BLACK
    } else {
        GameColor.WHITE
    }
    LaunchedEffect(rememberedOnlineGame) {
        while (true) {
            onRefreshGame(onlineGame, localGame, color)
            delay(1000)
        }
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            "CHER",
            color = MaterialTheme.colorScheme.primary,
            style = TextStyle(fontWeight = FontWeight.Black, fontSize = 40.sp, textAlign = TextAlign.Center)
        )
        rememberedLocalGame.reversi.board.forEach { row ->
            Row {
                row.list.forEach { square ->
                    BoardPiece(localGame, onlineGame, square, color, onPlayMade)
                }
            }
        }
        GamePlayerInfo(currentPlayer, opponentPlayer, localGame.value, onlineGame.value, onSurrender)
        GameFinished(currentPlayer, rememberedLocalGame, onExit, onEndGame, onSaveGame)
    }
}
