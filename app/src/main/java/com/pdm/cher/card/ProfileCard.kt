package com.pdm.cher.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.cher.component.playerpage.UploadImage
import com.pdm.cher.data.Player
import kotlin.math.abs

@Composable
fun ProfileCard(player: Player, uploadImage: (Player, ByteArray, MutableState<ImageBitmap?>) -> Unit, getImage: (String, MutableState<ImageBitmap?>) -> Unit, bitmapState: MutableState<ImageBitmap?>) {
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        Text("${player.username}${player.randomId}", style = TextStyle(fontWeight = FontWeight.Black, fontSize = 40.sp))
        Spacer(modifier = Modifier.height(8.dp))
        player.getImage(bitmapState, getImage)
        UploadImage(uploadImage, player, bitmapState)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Text("Wins: ${player.wins}",  style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
            Text("Losses: ${player.losses}",  style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
            Text("Draws: ${abs(player.wins - player.losses)}",  style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
        }
    }
}