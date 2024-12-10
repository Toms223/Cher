package com.pdm.cher.viewmodels

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pdm.cher.data.Player
import com.pdm.cher.firebase.ReversiFirebase
import kotlinx.coroutines.launch


class PlayerInformationViewModel(firestore: FirebaseFirestore, auth: FirebaseAuth, storage: FirebaseStorage): ViewModel() {
    private val reversiFirebase = ReversiFirebase(firestore, auth, storage)

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