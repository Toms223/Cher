package com.pdm.cher.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pdm.cher.data.GameRepetition
import com.pdm.cher.data.Player
import com.pdm.cher.screen.GameReplayScreen
import com.pdm.cher.ui.theme.CherTheme
import com.pdm.cher.viewmodels.FavoriteGamesViewModel
import com.pdm.cher.viewmodels.FirebaseResult
import com.pdm.cher.viewmodels.PlayerInformationViewModel
import kotlin.math.log


class GameReplayActivity: ComponentActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val favoriteGamesViewModel = FavoriteGamesViewModel()
    private val gameRepetition = mutableStateOf(GameRepetition())
    private val playerInformationViewModel = PlayerInformationViewModel(firestore, auth, storage)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        val gameName = intent.getStringExtra("gameName") ?: ""
        val currentPlayer = intent.getParcelableExtra<Player>("player") ?: run {
            finish()
            return
        }
        val fileInputStream = openFileInput("$gameName-Game.txt")
        gameRepetition.value = favoriteGamesViewModel.loadGameRepetition(fileInputStream)
        val opponentPlayer = mutableStateOf(Player("Unknown", gameRepetition.value.opponentEmail))
        setContent {
            CherTheme {
                GameReplayScreen(
                    gameRepetition,
                    ::getPlayer,
                    currentPlayer,
                    opponentPlayer
                )
            }
        }
    }

    private fun getPlayer(opponentPlayer: MutableState<Player>) {
        playerInformationViewModel.getPlayer(opponentPlayer.value.email) {
            when (it) {
                is FirebaseResult.Success -> {
                    opponentPlayer.value = it.data
                }
                is FirebaseResult.Error -> {
                    Log.e("GameReplayActivity", it.message ?: "Player not found")
                }
            }
        }
    }


}