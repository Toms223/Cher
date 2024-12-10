package com.pdm.cher.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pdm.cher.data.Game
import com.pdm.cher.reversi.Square
import com.pdm.cher.reversi.Color as GameColor

private val cellSize = 40.dp
@Composable
fun BoardReplayPiece(
    square: Square
){
    Box(
        modifier = Modifier
            .size(cellSize)
            .background(MaterialTheme.colorScheme.primary)
            .border(1.dp, Color.Black),
        contentAlignment = Alignment.Center
    ) {
        DrawReplayPiece(square)
    }
}