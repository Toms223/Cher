package com.pdm.cher.component

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pdm.cher.R
import com.pdm.cher.data.Game
import com.pdm.cher.reversi.Reversi
import com.pdm.cher.reversi.Square
import com.pdm.cher.reversi.Color as GameColor

private val cellSize = 40.dp

@Composable
fun BoardPiece(
    localGame: MutableState<Game>,
    onlineGame: MutableState<Game>,
    square: Square,
    playerColor: GameColor,
    onPlayMade: (Game, GameColor, Int, Int) -> Unit)
{
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .size(cellSize)
            .background(MaterialTheme.colorScheme.primary)
            .clickable {
                try {
                    localGame.value = localGame.value.copy(reversi = localGame.value.reversi.makePlay(square.copy(color = playerColor)))
                    onPlayMade(onlineGame.value, playerColor, square.row, square.col)
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }

            }
            .border(1.dp, Color.Black),
        contentAlignment = Alignment.Center
    ) {
       DrawPiece(square, localGame.value.reversi, playerColor)
    }
}


@Composable
fun DrawPiece(square: Square, reversi: Reversi, playerColor: GameColor) {
    val piece = when (square.color) {
        com.pdm.cher.reversi.Color.BLACK -> painterResource(R.drawable.black_piece)
        com.pdm.cher.reversi.Color.WHITE -> painterResource(R.drawable.white_piece)
        else -> null
    }
    if (piece != null){
        Image(painter = piece, contentDescription = "", modifier = Modifier.size(30.dp))
    } else {
        if(reversi.getValidPlaySquares(playerColor, reversi.board).contains(square) && playerColor == reversi.currentPlayer) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
            )
        }
    }
}