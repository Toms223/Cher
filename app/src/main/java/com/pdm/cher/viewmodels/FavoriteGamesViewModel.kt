package com.pdm.cher.viewmodels

import androidx.lifecycle.ViewModel
import com.pdm.cher.data.GameRepetition
import com.pdm.cher.reversi.Color
import com.pdm.cher.reversi.ReversiRepetition
import com.pdm.cher.toListSquares
import java.io.FileInputStream

class FavoriteGamesViewModel: ViewModel() {
    fun loadGameRepetition(fileInputStream: FileInputStream): GameRepetition {
        val output = fileInputStream.bufferedReader().use { it.readText() }.split(" ")
        val color = output[0].let { when(it){
            "black" -> Color.BLACK
            "white" -> Color.WHITE
            else -> Color.BLACK
        } }
        val email = output[1]
        val listSquares = output[2].toListSquares()
        return GameRepetition(color, email, ReversiRepetition(listSquares))
    }

    fun getAvailableRepetitions(files: Array<String>): List<String> {
        return files.toList().filter { it.contains("-Game.txt") }.map { it.removeSuffix("-Game.txt").split("T").joinToString(" ") }
    }
}