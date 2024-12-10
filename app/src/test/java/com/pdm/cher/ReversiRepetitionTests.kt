package com.pdm.cher

import com.pdm.cher.reversi.ReversiRepetition
import com.pdm.cher.reversi.Reversi
import org.junit.jupiter.api.Assertions
import org.junit.Test
import kotlin.test.assertEquals

class ReversiRepetitionTests {

    @Test
    fun `Should repeat plays and arrive at end state`(){
        var game = Reversi()
        while(game.state.name != "FINISHED" && game.state.name != "DRAW"){
            val validSquares = game.getValidPlaySquares(game.currentPlayer, game.board)
            if (validSquares.isEmpty()) {
                game = game.skipTurn()
                continue
            }
            val play = validSquares.random().copy(color = game.currentPlayer)
            game = game.makePlay(play)
        }
        var reversiRepetition = ReversiRepetition(game.playsOrder)
        while(reversiRepetition.currentState.state.name != "FINISHED" && reversiRepetition.currentState.state.name != "DRAW"){
            reversiRepetition = reversiRepetition.next()
        }
        Assertions.assertEquals(game, reversiRepetition.currentState)
    }

    @Test
    fun `Should repeat plays and arrive at surrender state`(){
        var game = Reversi()
        game = game.surrender(game.currentPlayer)
        val reversiRepetition = ReversiRepetition(game.playsOrder).next()
        Assertions.assertEquals(game, reversiRepetition.currentState)
    }

    @Test
    fun `Should return to beginning of game`(){
        var game = Reversi()
        val gameStates = mutableListOf(game)
        while(game.state.name != "FINISHED" && game.state.name != "DRAW"){
            val validSquares = game.getValidPlaySquares(game.currentPlayer, game.board)
            if (validSquares.isEmpty()) {
                game = game.skipTurn()
                gameStates.add(game)
                continue
            }
            val play = validSquares.random().copy(color = game.currentPlayer)
            game = game.makePlay(play)
            gameStates.add(game)
        }
        var reversiRepetition = ReversiRepetition(game.playsOrder)
        while(reversiRepetition.currentState.state.name != "FINISHED" && reversiRepetition.currentState.state.name != "DRAW"){
            reversiRepetition = reversiRepetition.next()
        }
        Assertions.assertEquals(game, reversiRepetition.currentState)
        while(reversiRepetition.currentState != gameStates.first()){
            assertEquals(gameStates.last(), reversiRepetition.currentState)
            reversiRepetition = reversiRepetition.previous()
            if(gameStates.isNotEmpty()) gameStates.removeLast()
        }
    }
}