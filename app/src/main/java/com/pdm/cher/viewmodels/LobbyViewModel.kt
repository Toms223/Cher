package com.pdm.cher.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pdm.cher.data.Game
import com.pdm.cher.data.Invite
import com.pdm.cher.data.Player
import com.pdm.cher.firebase.ReversiFirebase
import kotlinx.coroutines.launch

class LobbyViewModel(firestore: FirebaseFirestore, auth: FirebaseAuth, storage: FirebaseStorage): ViewModel() {
    private val reversiFirebase = ReversiFirebase(firestore, auth, storage)
    fun enterLobby(player: Player, onResult: (FirebaseResult<List<Player>>) -> Unit) = viewModelScope.launch {
        try {
            reversiFirebase.enterLobby(player)
            onResult(FirebaseResult.Success(reversiFirebase.getLobby()))
        } catch (e: Exception) {
            onResult(FirebaseResult.Error(e.message))
        }

    }

    fun getLobby(onResult: (FirebaseResult<List<Player>>) -> Unit) = viewModelScope.launch {
        try {
            onResult(FirebaseResult.Success(reversiFirebase.getLobby()))
        } catch (e: Exception) {
            onResult(FirebaseResult.Error(e.message))
        }
    }

    fun leaveLobby(player: Player, onResult: (FirebaseResult<String>) -> Unit) = viewModelScope.launch {
        try {
            reversiFirebase.leaveLobby(player)
            onResult(FirebaseResult.Success("Player left the lobby"))
        } catch (e: Exception) {
            onResult(FirebaseResult.Error(e.message))
        }
    }

    fun sendInvite(invitingPlayer: Player, invitedPlayer: Player, onResult: (FirebaseResult<String>) -> Unit) = viewModelScope.launch {
        try {
            reversiFirebase.invitePlayer(invitingPlayer, invitedPlayer)
            onResult(FirebaseResult.Success("Invite sent"))
        } catch (e: Exception) {
            onResult(FirebaseResult.Error(e.message))
        }
    }

    fun getInvite(invitedPlayer: Player, onResult: (FirebaseResult<Invite>) -> Unit) = viewModelScope.launch {
        try {
            onResult(FirebaseResult.Success(reversiFirebase.getInvite(invitedPlayer)))
        } catch (e: Exception) {
            onResult(FirebaseResult.Error(e.message))
        }
    }

    fun acceptInvite(currentPlayer: Player, onResult: (FirebaseResult<String>) -> Unit) = viewModelScope.launch {
        try {
            reversiFirebase.acceptInvite(currentPlayer)
            onResult(FirebaseResult.Success("Invite accepted"))
        } catch (e: Exception) {
            onResult(FirebaseResult.Error(e.message))
        }
    }

    fun declineInvite(currentPlayer: Player, onResult: (FirebaseResult<String>) -> Unit) = viewModelScope.launch {
        try {
            reversiFirebase.declineInvite(currentPlayer)
            onResult(FirebaseResult.Success("Invite declined"))
        } catch (e: Exception) {
            onResult(FirebaseResult.Error(e.message))
        }
    }

    fun getGameId(player: Player, onResult: (FirebaseResult<String>) -> Unit) = viewModelScope.launch {
        try {
            onResult(FirebaseResult.Success(reversiFirebase.getGameIdByPlayerEmail(player.email)))
        } catch (e: Exception) {
            onResult(FirebaseResult.Error(e.message))
        }
    }

    fun startGame(invitingPlayer: Player, invitedPlayer: Player, onResult: (FirebaseResult<String>) -> Unit) = viewModelScope.launch {
        try {
            onResult(FirebaseResult.Success(reversiFirebase.startGame(invitingPlayer, invitedPlayer)))
        } catch (e: Exception) {
            onResult(FirebaseResult.Error(e.message))
        }
    }
}