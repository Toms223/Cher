package com.pdm.cher.reversi

data class Reversi(val board: List<List<Square>> = makeEmptyBoard(),
                   val currentPlayer: Color = Color.BLACK,
                   val state: STATE = STATE.PLAYING,
                   val winner: Color? = null,
                   val playsOrder: List<Square> = listOf()
) {
    enum class STATE {
        PLAYING,
        SKIPPED,
        FINISHED,
        DRAW,
    }

    private val directions = listOf(
        Pair(-1, 0), Pair(1, 0),  // Vertical
        Pair(0, -1), Pair(0, 1), // Horizontal
        Pair(-1, -1), Pair(-1, 1), Pair(1, -1), Pair(1, 1) // Diagonal
    )

    fun makePlay(newSquare: Square): Reversi {
        if(newSquare.color == null) throw ReversiException.InvalidPosition()
        if(newSquare.color != currentPlayer) throw ReversiException.NotYourTurn()
        val validPositions = getValidPlaySquares(currentPlayer, this.board)
        if(validPositions.none { square -> square.row == newSquare.row && square.col == newSquare.col })
            throw ReversiException.InvalidPosition()
        val playedBoard = board.map { row ->
            row.map { square -> if(newSquare.col == square.col && newSquare.row == square.row) newSquare else square
            }
        }
        val newBoard = convertOpponentPieces(playedBoard, newSquare)
        val validPositionsAfterPlay = getValidPlaySquares(currentPlayer, newBoard)
        val opponentValidPositions = getValidPlaySquares(currentPlayer.opposite(), newBoard)
        if(validPositionsAfterPlay.isEmpty() && opponentValidPositions.isEmpty()) return endGame(newBoard, newSquare)
        return this.copy(board = newBoard, playsOrder = playsOrder + newSquare, currentPlayer = currentPlayer.opposite())
    }

    fun skipTurn(): Reversi {
        if(state == STATE.SKIPPED) return endGame(this.board, Square(-1, -1, null))
        return this.copy(currentPlayer = currentPlayer.opposite(), playsOrder = playsOrder + Square(-1, -1, null), state = STATE.SKIPPED)
    }

    private fun endGame(board: List<List<Square>>, lastPlay: Square): Reversi{
        val blackPieces = board.flatMap { row -> row.filter { square -> square.color == Color.BLACK } }.count()
        val whitePieces = board.flatMap { row -> row.filter { square -> square.color == Color.WHITE } }.count()
        val returnGame = this.copy(board = board, playsOrder = playsOrder + lastPlay, currentPlayer = currentPlayer.opposite())
        return if(blackPieces > whitePieces) returnGame.copy(state = STATE.FINISHED, winner = Color.BLACK)
        else if(whitePieces > blackPieces) returnGame.copy(state = STATE.FINISHED, winner = Color.WHITE)
        else returnGame.copy(state = STATE.DRAW)
    }

    fun getValidPlaySquares(playerColor: Color, board: List<List<Square>>) : List<Square> {
        val filledSquares = board.flatMap { row -> row.filter { it.color != null } }
        val potentialSquares = filledSquares.flatMap { square ->
            directions.mapNotNull { direction ->
                val newRow = square.row + direction.first
                val newCol = square.col + direction.second
                if (newRow in board.indices && newCol in board[newRow].indices) {
                    val neighbor = board[newRow][newCol]
                    if (neighbor.color == null) neighbor else null
                } else null
            }
        }.distinct()

        return potentialSquares.filter { square ->
            directions.any { direction ->
                val positions = generateDirectionalPositions(square, direction)
                val opponentSquares = positions.takeWhile { (x, y) ->
                    board[x][y].color == playerColor.opposite()
                }
                if (opponentSquares.isNotEmpty()) {
                    val (lastX, lastY) = opponentSquares.last()
                    if (lastX + direction.first in board.indices && lastY + direction.second in board[lastX].indices) {
                        val nextSquare = board[lastX + direction.first][lastY + direction.second]
                        nextSquare.color == playerColor
                    } else false
                } else false
            }
        }
    }

    private fun convertOpponentPieces(newBoard: List<List<Square>>, playedSquare: Square): List<List<Square>>{
        val newSquares: MutableList<Pair<Int,Int>> = mutableListOf()
        for(direction in directions){
            val directionalPositions = generateDirectionalPositions(playedSquare, direction).filter{
                    (x, y) -> board[x][y].color != null
            }
            val currentPlayerPieces = directionalPositions.filter { (x, y) -> newBoard[x][y].color == currentPlayer }
            if(currentPlayerPieces.isNotEmpty()){
                val secondPlayerPiece = currentPlayerPieces.first()
                val opponents = directionalPositions.filter { (x, y) ->
                    newBoard[x][y].color != currentPlayer && isBefore(Pair(x,y),Pair(secondPlayerPiece.first,secondPlayerPiece.second), direction)
                }
                if(opponents.isNotEmpty()) {
                    newSquares.addAll(opponents)
                }
            }
        }
        return newBoard.map { row ->
            row.map { square ->
                if (newSquares.any { newSquare -> newSquare.first == square.row && newSquare.second == square.col }) Square(
                    square.row,
                    square.col,
                    currentPlayer
                )
                else square
            }
        }
    }

    private fun generateDirectionalPositions(square: Square, direction: Pair<Int, Int>): List<Pair<Int, Int>> {
        return generateSequence(Pair(square.row + direction.first, square.col + direction.second)) {
            Pair(it.first + direction.first, it.second + direction.second)
        }.takeWhile {
            it.first in board.indices && it.second in board[it.first].indices
        }.toList()

    }
}

