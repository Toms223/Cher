package com.pdm.cher.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.pdm.cher.component.*
import com.pdm.cher.data.GameRepetition
import com.pdm.cher.data.Player

@Composable
fun GameReplayScreen(
    repetitionState: MutableState<GameRepetition>,
    onGetPlayer: (MutableState<Player>) -> Unit,
    currentPlayer: Player,
    opponentPlayer:MutableState<Player>,
) {
    LaunchedEffect(Unit) {
        onGetPlayer(opponentPlayer)
    }
    var gameRepetition by remember { repetitionState }
    Log.d("GameReplayActivity", "State updated: ${gameRepetition.reversiRepetition.currentState}")
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
        gameRepetition.reversiRepetition.currentState.board.forEach { row ->
            Row {
                row.list.forEach { square ->
                    BoardReplayPiece(square)
                }
            }
        }
        GameReplayPlayerInfo(currentPlayer, opponentPlayer.value, repetitionState.value.opponentColor)
        Button(onClick = {
            gameRepetition =
                gameRepetition.copy(
                    reversiRepetition = gameRepetition.reversiRepetition.next()
                )
            repetitionState.value = gameRepetition
        }) {
            Icon(Icons.AutoMirrored.Default.KeyboardArrowRight, contentDescription = "Next")
        }
        Button(onClick = {
            repetitionState.value =
                gameRepetition.copy(
                    reversiRepetition = gameRepetition.reversiRepetition.previous()
                )
        }) {
            Icon(Icons.AutoMirrored.Default.KeyboardArrowLeft, contentDescription = "Previous")
        }
    }
}