package com.pdm.cher.component.lobby


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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.cher.data.Invite
import com.pdm.cher.data.Player

@Composable
fun ReceivedInvite(invite: Invite?, currentPlayer: Player, invitingPlayer: Player?, onInviteAccept: (Player) -> Unit, onInviteDecline: (Player) -> Unit){
    if (invite != null && invitingPlayer != null){
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f))
        )
        {
            Text("You have an invite from ${invitingPlayer.username}${invitingPlayer.randomId}", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp, textAlign = TextAlign.Center))
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = {
                    onInviteAccept(currentPlayer)
                },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Accept")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    onInviteDecline(currentPlayer)
                },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onError)
                ) {
                    Text("Decline")
                }
            }
        }
    }
}