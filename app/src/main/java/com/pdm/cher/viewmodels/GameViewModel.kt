package com.pdm.cher.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm.cher.data.Game
import com.pdm.cher.data.Player
import com.pdm.cher.firebase.ReversiFirebase
import com.pdm.cher.reversi.Color
import com.pdm.cher.reversi.Square
import kotlinx.coroutines.launch

class GameViewModel: ViewModel() {
    private val reversiFirebase = ReversiFirebase.getInstance()

    fun getGameById(gameId: String, onResult: (FirebaseResult<Game>) -> Unit) = viewModelScope.launch {
        try {
            onResult(FirebaseResult.Success(reversiFirebase.getGameById(gameId) ?: throw Exception("Game not found")))
        }
        catch (e: Exception) {
            onResult(FirebaseResult.Error(e.message))
        }
    }

    fun surrender(game: Game, color: Color, onResult: (FirebaseResult<String>) -> Unit) = viewModelScope.launch {
        try {
            val newReversi = game.reversi.surrender(color)
            reversiFirebase.makePlay(game.copy(reversi = newReversi))
            onResult(FirebaseResult.Success("Surrender Made"))
        }
        catch (e: Exception) {
            onResult(FirebaseResult.Error(e.message))
        }
    }

    fun skipTurn(game: Game, onResult: (FirebaseResult<String>) -> Unit) = viewModelScope.launch {
        try {
            val newReversi = game.reversi.skipTurn()
            reversiFirebase.makePlay(game.copy(reversi = newReversi))
            onResult(FirebaseResult.Success("Turn Skipped"))
        }
        catch (e: Exception) {
            onResult(FirebaseResult.Error(e.message))
        }
    }

    fun makePlay(game: Game, color: Color, row: Int, column: Int, onResult: (FirebaseResult<String>) -> Unit) = viewModelScope.launch {
        try {
            val square = Square(row, column, color)
            val newReversi = game.reversi.makePlay(square)
            reversiFirebase.makePlay(game.copy(reversi = newReversi))
            onResult(FirebaseResult.Success("Play Made"))
        }
        catch (e: Exception) {
            onResult(FirebaseResult.Error(e.message))
        }
    }

    fun endGame(game: Game, onResult: (FirebaseResult<String>) -> Unit) = viewModelScope.launch {
        try {
            reversiFirebase.endGame(game)
            onResult(FirebaseResult.Success("Game Ended"))
        }
        catch (e: Exception) {
            onResult(FirebaseResult.Error(e.message))
        }
    }
}