package com.pdm.cher.reversi

data class GameRepetition(private val playsMade: List<Square>, private var currentState: Reversi = Reversi(), private var index: Int = 0) {
    fun next(): Reversi {
        if(index >= playsMade.size) return currentState
        val nextPlay = playsMade[index]
        currentState = if(nextPlay.row == -1 && nextPlay.col == -1){
            currentState.skipTurn()
        } else {
            currentState.makePlay(playsMade[index])
        }
        index++
        return currentState
    }

    fun current(): Reversi {
        return currentState
    }
}