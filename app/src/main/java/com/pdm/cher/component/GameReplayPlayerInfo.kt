package com.pdm.cher.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pdm.cher.R
import com.pdm.cher.data.Game
import com.pdm.cher.data.Player
import com.pdm.cher.reversi.Color

@Composable
fun GameReplayPlayerInfo(currentPlayer: Player, opponentPlayer: Player, opponentColor: Color) {
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.inversePrimary)
    ) {
        ReplayPlayerCardInfo(currentPlayer, opponentColor.opposite())
        ReplayPlayerCardInfo(opponentPlayer, opponentColor)
    }
}

@Composable
fun ReplayPlayerCardInfo(player: Player, color: Color) {
    val playerPiece = if (color == Color.BLACK) {
        painterResource(R.drawable.black_piece)
    } else {
        painterResource(R.drawable.white_piece)
    }
    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Text(
            player.username + player.randomId,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = playerPiece,
            contentDescription = "",
            modifier = Modifier.size(20.dp)
        )
    }
}