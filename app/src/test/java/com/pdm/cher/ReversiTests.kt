package com.pdm.cher


import com.pdm.cher.reversi.*
import org.junit.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

class ReversiTests {

    @Test
    fun `getValidPlaySquares should return correct squares for initial state`() {
        val game = Reversi()

        val validSquares = game.getValidPlaySquares(Color.BLACK, game.board)

        // Validate that the correct squares are returned
        assertEquals(4, validSquares.size)
        assertTrue(validSquares.contains(Square(2, 4, null)))
        assertTrue(validSquares.contains(Square(3, 5, null)))
        assertTrue(validSquares.contains(Square(4, 2, null)))
        assertTrue(validSquares.contains(Square(5, 3, null)))
    }

    @Test
    fun `makePlay should update board and switch player for a valid single switch move`() {
        val game = Reversi()

        val newSquare = Square(2, 4, Color.BLACK)
        val newGame = game.makePlay(newSquare)

        // Verify the board was updated
        assertEquals(Color.BLACK, newGame.board[2].list[4].color)
        assertEquals(Color.BLACK, newGame.board[3].list[4].color) // Flipped piece
        assertEquals(Color.BLACK, newGame.board[4].list[4].color) // Unchanged piece
        assertEquals(Color.BLACK, newGame.board[3].list[3].color)
        assertEquals(Color.WHITE, newGame.board[4].list[3].color)

        // Verify the current player switched
        assertEquals(Color.WHITE, newGame.currentPlayer)

    }

    @Test
    fun `makePlay should update board and switch player for multi-piece and multi-line flips`() {
        val board = makeEmptyBoard().map { it.list.toMutableList() }
        board[3][3] = Square(3, 3, null)
        board[3][4] = Square(3, 4, Color.WHITE)
        board[3][5] = Square(3, 5, Color.WHITE)
        board[3][6] = Square(3, 5, Color.BLACK)

        board[4][3] = Square(4, 3, Color.WHITE)
        board[5][3] = Square(5, 3, Color.WHITE)
        board[6][3] = Square(5, 3, Color.BLACK)

        board[4][4] = Square(4, 4, Color.WHITE)
        board[5][5] = Square(5, 5, Color.WHITE)
        board[6][6] = Square(5, 5, Color.BLACK)

        val game = Reversi(board.map { GameRow(it) })
        val newSquare = Square(3, 3, Color.BLACK) // Bottom-right corner move

        // Act: Perform the play
        val newGame = game.makePlay(newSquare)

        // Assert: Verify pieces were flipped in all directions
        // Horizontal
        assertEquals(Color.BLACK, newGame.board[3].list[4].color)
        assertEquals(Color.BLACK, newGame.board[3].list[5].color)
        // Vertical
        assertEquals(Color.BLACK, newGame.board[4].list[3].color)
        assertEquals(Color.BLACK, newGame.board[5].list[3].color)
        // Diagonal (SE)
        assertEquals(Color.BLACK, newGame.board[4].list[4].color)
        assertEquals(Color.BLACK, newGame.board[5].list[5].color)
        // New piece
        assertEquals(Color.BLACK, newGame.board[3].list[3].color)

        // Verify the current player switched
        assertEquals(Color.WHITE, newGame.currentPlayer)
    }


    @Test
    fun `makePlay should throw InvalidPosition for a square with no piece`() {
        val game = Reversi()

        val invalidSquare = Square(2, 2, null)

        val exception = assertThrows<ReversiException.InvalidPosition> {
            game.makePlay(invalidSquare)
        }
        assertEquals("Invalid Position", exception.message)
    }

    @Test
    fun `makePlay should throw InvalidPosition for an invalid move`() {
        val game = Reversi()

        val invalidSquare = Square(0, 0, Color.BLACK)

        val exception = assertThrows<ReversiException.InvalidPosition> {
            game.makePlay(invalidSquare)
        }
        assertEquals("Invalid Position", exception.message)
    }

    @Test
    fun `makePlay should throw NotYourTurn when trying to play out of turn`() {
        val game = Reversi()

        val newSquare = Square(2, 4, Color.WHITE)

        val exception = assertThrows<ReversiException.NotYourTurn> {
            game.makePlay(newSquare)
        }
        assertEquals("Not Your Turn", exception.message)
    }

    @Test
    fun `makePlay should end the game when the board is full and declare a winner`() {
        // Arrange: Create a nearly full board with Black dominating
        val board = makeEmptyBoard().map{ it.list }.mapIndexed { row, cols ->
            cols.mapIndexed { col, square ->
                when {
                    row == 7 && col == 7 -> square // Leave one spot empty
                    row % 2 == 0 -> square.copy(color = Color.BLACK)
                    else -> square.copy(color = Color.WHITE)
                }
            }.toMutableList()
        }
        board[7][1] = Square(7, 1, Color.BLACK)
        val game = Reversi(board = board.map { GameRow(it) }, currentPlayer = Color.BLACK)

        // Act: Black plays the final move to fill the board
        val newSquare = Square(7, 7, Color.BLACK)
        val newGame = game.makePlay(newSquare)

        // Assert: Game should end and Black should be the winner
        assertEquals(GameState.FINISHED, newGame.state)
        assertEquals(Color.BLACK, newGame.winner)
    }

    @Test
    fun `makePlay should end the game with a draw when both players have the same number of pieces`() {
        // Arrange: Create a nearly full board with equal pieces
        val board = makeEmptyBoard().map{ it.list }.mapIndexed { row, cols ->
            cols.mapIndexed { col, square ->
                when {
                    row == 7 && col == 7 -> square // Leave one spot empty
                    (row + col) % 2 == 0 -> square.copy(color = Color.BLACK)
                    else -> square.copy(color = Color.WHITE)
                }
            }.toMutableList()
        }
        board[7][1] = Square(7, 1, Color.WHITE)
        board[7][3] = Square(7, 3, Color.WHITE)
        val game = Reversi(board = board.map { GameRow(it) }, currentPlayer = Color.BLACK)

        // Act: Black plays the final move to fill the board
        val newSquare = Square(7, 7, Color.BLACK)
        val newGame = game.makePlay(newSquare)

        // Assert: Game should end with a draw
        assertEquals(GameState.DRAW, newGame.state)
        assertNull(newGame.winner)
    }

    @Test
    fun `makePlay should end the game after consecutive skips`() {
        // Arrange: Set up a board where no valid moves are available for both players
        val board = makeEmptyBoard().map{ it.list }.mapIndexed { row, cols ->
            cols.mapIndexed { col, square ->
                when {
                    row < 6 || row == 6 && col == 1 -> square.copy(color = Color.WHITE)
                    row == 6 && col > 1 && col < 7-> square.copy(color = Color.BLACK)
                    else -> square
                }
            }
        }
        val game = Reversi(board = board.map { GameRow(it) }, currentPlayer = Color.WHITE, state = GameState.PLAYING)

        // Act: Black skips, triggering game end
        val newGame = game.makePlay(Square(6, 7, Color.WHITE)) // Any invalid move triggers skip

        // Assert: Game should end with White as the winner
        assertEquals(GameState.FINISHED, newGame.state)
        assertEquals(Color.WHITE, newGame.winner)
    }

    @Test
    fun `surrender should end the game and declare the opponent as the winner`() {
        val game = Reversi()

        val newGame = game.surrender(Color.BLACK)

        assertEquals(GameState.SURRENDER, newGame.state)
        assertEquals(Color.WHITE, newGame.winner)
    }
}
