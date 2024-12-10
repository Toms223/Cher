package com.pdm.cher.card

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.cher.data.Player
import com.pdm.cher.component.Logo
import com.pdm.cher.component.MovingBackground


@Composable
fun HomeCard(player: Player, onEnterLobby: (Player) -> Unit) {
     MovingBackground{
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Logo()
            Spacer(modifier = Modifier.height(8.dp))
            Text("CHER", style = TextStyle(fontWeight = FontWeight.Black, fontSize = 40.sp, textAlign = TextAlign.Center))
            Text("Reversi by Chelas", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 25.sp, textAlign = TextAlign.Center))
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {onEnterLobby(player)}) {
                Text("Play")
            }
        }
    }
}