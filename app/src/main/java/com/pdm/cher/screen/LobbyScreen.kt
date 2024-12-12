package com.pdm.cher.screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.cher.component.lobby.LobbyPlayer
import com.pdm.cher.component.MovingBackground
import com.pdm.cher.component.lobby.ReceivedInvite
import com.pdm.cher.data.Invite
import com.pdm.cher.data.Player
import kotlinx.coroutines.delay


@Composable
fun LobbyScreen(
    currentPlayer: Player,
    invitingPlayer: MutableState<Player?>,
    invitedPlayer: MutableState<Player?>,
    lobbyPlayers: MutableState<List<Player>>,
    receivedInvite: MutableState<Invite?>,
    gameStarted : MutableState<Boolean>,
    retrieveOnGoingGame: (Player) -> Unit,
    checkSentInvite: (Player, Player?) -> Unit,
    refreshLobby: () -> Unit,
    refreshReceivedInvite: (Player) -> Unit,
    onImageRetrieve: (String, MutableState<ImageBitmap?>) -> Unit,
    onSendInvite: (Player, Player) -> Unit,
    onInviteAccept: (Player) -> Unit,
    onInviteDecline: (Player) -> Unit
) {
    val rememberInvitedPlayer by remember { invitedPlayer }
    val rememberInvitingPlayer by remember { invitingPlayer }
    val rememberLobbyPlayers by remember { lobbyPlayers }
    LaunchedEffect(Unit) {
        while(!gameStarted.value){
            retrieveOnGoingGame(currentPlayer)
            delay(2000L)
        }
    }
    LaunchedEffect(lobbyPlayers) {
        while (true){
            refreshLobby()
            delay(2000L)
        }
    }
    LaunchedEffect(Unit) {
        while (true){
            refreshReceivedInvite(currentPlayer)
            delay(2000L)
        }
    }
    LaunchedEffect(Unit) {
        while(true){
            checkSentInvite(currentPlayer, rememberInvitedPlayer)
            delay(2000L)
        }
    }
    MovingBackground {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Lobby Players",  style = TextStyle(
                fontWeight = FontWeight.Black,
                fontSize = 50.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier.fillMaxWidth().padding(8.dp).align(Alignment.TopCenter)
            )
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()) {
                items(rememberLobbyPlayers.filter { it != currentPlayer }) { lobbyPlayer ->
                    LobbyPlayer(currentPlayer, lobbyPlayer, onImageRetrieve, onSendInvite)
                }
            }
            ReceivedInvite(receivedInvite.value, currentPlayer, rememberInvitingPlayer, onInviteAccept, onInviteDecline)
        }
    }
}
