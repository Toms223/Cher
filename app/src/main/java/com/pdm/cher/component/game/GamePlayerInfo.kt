package com.pdm.cher.component.game


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
fun GamePlayerInfo(currentPlayer: Player, opponentPlayer: Player, localGame: Game, onlineGame: Game, onSurrender: (Game, Color) -> Unit) {
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.inversePrimary)
    ) {
        PlayerCardInfo(currentPlayer, localGame)
        PlayerCardInfo(opponentPlayer, localGame)
    }
    Button(
        onClick = {
            val color = if (currentPlayer.email == localGame.playerBlackEmail) {
                Color.BLACK
            } else {
                Color.WHITE
            }
            onSurrender(onlineGame, color)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error
        )
    ) {
        Text("Surrender")
    }
}

@Composable
fun PlayerCardInfo(player: Player, game: Game){
    val playerPiece = if (player.email == game.playerBlackEmail) {
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