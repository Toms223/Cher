package com.pdm.cher.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pdm.cher.screen.PlayerPageScreen
import com.pdm.cher.data.Player
import com.pdm.cher.ui.theme.CherTheme
import com.pdm.cher.viewmodels.FavoriteGamesViewModel
import com.pdm.cher.viewmodels.FirebaseResult
import com.pdm.cher.viewmodels.PlayerInformationViewModel

class PlayerPageActivity: ComponentActivity() {
    private val bitmapState = mutableStateOf<ImageBitmap?>(null)
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val favoriteGamesViewModel = FavoriteGamesViewModel()
    private val userInformationViewModel = PlayerInformationViewModel(firestore, auth, storage)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        val player = intent.getParcelableExtra<Player>("player")
        if (player == null) {
            finish()
            return // Return because otherwise null-safety complains and I don't have a complaint book
        }
        setContent {
            CherTheme {
                PlayerPageScreen(
                    player,
                    ::enterLobby,
                    favoriteGamesViewModel::getAvailableRepetitions,
                    ::uploadPlayerImage,
                    ::getPlayerImage,
                    bitmapState
                )
            }
        }
    }

    private fun uploadPlayerImage(player: Player, image: ByteArray, bitmap: MutableState<ImageBitmap?>) {
        userInformationViewModel.uploadPlayerImage(player, image) {
            when (it) {
                is FirebaseResult.Success -> {
                    bitmap.value = it.data
                }
                is FirebaseResult.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getPlayerImage(email: String, bitmap: MutableState<ImageBitmap?>) {
        userInformationViewModel.getPlayerImage(email) {
            when (it) {
                is FirebaseResult.Success -> {
                    bitmap.value = it.data
                }
                is FirebaseResult.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun enterLobby(player: Player) {
        startActivity(Intent(this, LobbyActivity::class.java).putExtra("player", player))
    }
}