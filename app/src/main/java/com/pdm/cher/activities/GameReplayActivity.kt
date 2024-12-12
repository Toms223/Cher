package com.pdm.cher.activities

import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.pdm.cher.R
import com.pdm.cher.data.GameRepetition
import com.pdm.cher.data.Player
import com.pdm.cher.screen.GameReplayScreen
import com.pdm.cher.ui.theme.CherTheme
import com.pdm.cher.viewmodels.FavoriteGamesViewModel
import com.pdm.cher.viewmodels.FirebaseResult
import com.pdm.cher.viewmodels.PlayerInformationViewModel


class GameReplayActivity: ComponentActivity() {
    private val favoriteGamesViewModel = FavoriteGamesViewModel()
    private val gameRepetition = mutableStateOf(GameRepetition())
    private val playerInformationViewModel = PlayerInformationViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        val gameName = intent.getStringExtra("gameName") ?: ""
        val currentPlayer = intent.getParcelableExtra<Player>("player") ?: run {
            finish()
            return
        }
        val soundPool = SoundPool.Builder()
            .setMaxStreams(5) // Max simultaneous streams
            .build()
        val soundId = soundPool.load(this, R.raw.place_piece, 1)
        val fileInputStream = openFileInput("$gameName-Game.txt")
        gameRepetition.value = favoriteGamesViewModel.loadGameRepetition(fileInputStream)
        val opponentPlayer = mutableStateOf(Player("Unknown", gameRepetition.value.opponentEmail))
        setContent {
            CherTheme {
                GameReplayScreen(
                    gameRepetition,
                    currentPlayer,
                    opponentPlayer,
                    soundPool,
                    soundId,
                    ::playSound,
                    ::getPlayer,
                    ::finish
                )
            }
        }
    }

    private fun playSound(soundPool: SoundPool, soundId: Int){
        soundPool.play(soundId, 1F, 1F, 1, 0, 1F)
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