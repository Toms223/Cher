package com.pdm.cher.component


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.cher.data.Player


@Composable
fun LobbyPlayer(
    currentPlayer: Player,
    lobbyPlayer: Player,
    onImageRetrieve: (String, MutableState<ImageBitmap?>) -> Unit,
    onSendInvite: (Player, Player) -> Unit
) {
    val bitmapState = mutableStateOf<ImageBitmap?>(null)
    LaunchedEffect(bitmapState) {
        onImageRetrieve(lobbyPlayer.imageURL, bitmapState)
    }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clip(
            RoundedCornerShape(50.dp)
        ).background(MaterialTheme.colorScheme.onBackground))
    {
        lobbyPlayer.getImage(bitmapState, onImageRetrieve, 50.dp)
        Text("${lobbyPlayer.username}${lobbyPlayer.randomId}",  style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp, textAlign = TextAlign.Center), modifier = Modifier.align(
            Alignment.CenterVertically))
        Button(onClick = {
            onSendInvite(currentPlayer, lobbyPlayer)
        }) {
            Text("Invite")
        }
    }
}