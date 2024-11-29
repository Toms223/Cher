package com.pdm.cher

import com.pdm.cher.reversi.GameRepetition
import com.pdm.cher.reversi.Reversi
import org.junit.jupiter.api.Assertions
import org.junit.Test

class GameRepetitionTests {

    @Test
    fun `Should repeat plays and arrive at the same state as the game`(){
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
        val gameRepetition = GameRepetition(game.playsOrder)
        var repeatedGame = gameRepetition.current()
        while(repeatedGame.state.name != "FINISHED" && repeatedGame.state.name != "DRAW"){
            repeatedGame = gameRepetition.next()
        }
        Assertions.assertEquals(game, repeatedGame)
    }
}