package com.pdm.cher.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pdm.cher.R
import com.pdm.cher.reversi.Square

@Composable
fun DrawReplayPiece(square: Square) {
    val piece = when (square.color) {
        com.pdm.cher.reversi.Color.BLACK -> painterResource(R.drawable.black_piece)
        com.pdm.cher.reversi.Color.WHITE -> painterResource(R.drawable.white_piece)
        else -> null
    }
    if (piece != null){
        Image(painter = piece, contentDescription = "", modifier = Modifier.size(30.dp))
    }
}