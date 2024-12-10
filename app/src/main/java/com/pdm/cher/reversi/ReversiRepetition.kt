package com.pdm.cher.reversi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ReversiRepetition(private val playsMade: List<Square> = listOf(), val currentState: Reversi = Reversi(), private val index: Int = 0) :
    Parcelable {
    fun next(): ReversiRepetition {
        if(index >= playsMade.size) return this
        val nextPlay = playsMade[index]
        val newCurrentState = if(nextPlay.row == -1 && nextPlay.col == -1){
            currentState.skipTurn()
        } else if(nextPlay.row == -2 && nextPlay.col == -2){
            currentState.surrender(nextPlay.color!!)
        } else {
            currentState.makePlay(playsMade[index])
        }
        val newIndex = index + 1
        return ReversiRepetition(playsMade, newCurrentState, newIndex)
    }

    fun previous(): ReversiRepetition {
        if(index < 0) return this
        if(index == 0) return this.copy(currentState = Reversi())
        val newIndex = index - 1
        var newCurrentState = Reversi()
        for (i in 0 until newIndex){
            val currentPlay = playsMade[i]
            newCurrentState = if(currentPlay.row == -1 && currentPlay.col == -1){
                newCurrentState.skipTurn()
            } else if(currentPlay.row == -2 && currentPlay.col == -2){
                newCurrentState.surrender(currentPlay.color!!)
            } else {
                newCurrentState.makePlay(currentPlay)
            }
        }
        return ReversiRepetition(playsMade, newCurrentState, newIndex)
    }
}