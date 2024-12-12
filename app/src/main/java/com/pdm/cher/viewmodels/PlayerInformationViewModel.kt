package com.pdm.cher.viewmodels

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm.cher.data.Player
import com.pdm.cher.firebase.ReversiFirebase
import kotlinx.coroutines.launch


class PlayerInformationViewModel: ViewModel() {
    private val reversiFirebase = ReversiFirebase.getInstance()

    fun getPlayer(email: String, onResult: (FirebaseResult<Player>) -> Unit) = viewModelScope.launch {
        try {
            val player = reversiFirebase.getPlayer(email)
            if(player != null) {
                onResult(FirebaseResult.Success(player))
            } else {
                onResult(FirebaseResult.Error("Player not found"))
            }
        } catch (e: Exception) {
            onResult(FirebaseResult.Error(e.message))
        }
    }

    fun uploadPlayerImage(player: Player, bitmap: ByteArray, onResult: (FirebaseResult<ImageBitmap>) -> Unit) = viewModelScope.launch {
        try {
            val image = reversiFirebase.uploadPlayerImage(player, bitmap)
            onResult(FirebaseResult.Success(image))
        } catch (e: Exception) {
            onResult(FirebaseResult.Error(e.message))
        }
    }

    fun getPlayerImage(email: String, onResult: (FirebaseResult<ImageBitmap>) -> Unit) = viewModelScope.launch {
        try {
            val playerImage = reversiFirebase.getPlayerImage(email)
            onResult(FirebaseResult.Success(playerImage))
        } catch (e: Exception) {
            onResult(FirebaseResult.Error(e.message))
        }
    }
}