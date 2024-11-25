package com.pdm.cher.reversi

data class Reversi(val board: List<List<Square>> = makeEmptyBoard(),
                   val currentPlayer: Player = Player(Color.BLACK)
) {
    private val directions = listOf(
        Pair(-1, 0), Pair(1, 0),  // Vertical
        Pair(0, -1), Pair(0, 1), // Horizontal
        Pair(-1, -1), Pair(-1, 1), Pair(1, -1), Pair(1, 1) // Diagonal
    )

    fun makePlay(newSquare: Square): Reversi {
        if(newSquare.piece == null) throw ReversiException.InvalidPosition()
        if(!getValidPlaySquares().contains(newSquare)) throw ReversiException.InvalidPosition()
        if(newSquare.piece.color != currentPlayer.color) throw ReversiException.NotYourTurn()
        val newBoard = board.map { row -> row.map { square -> if(newSquare.col == square.col && newSquare.row == square.row) newSquare else square } }
        return this.copy(board = newBoard)
    }

    fun getValidPlaySquares() : List<Square> {
        val validSquares = board.flatMap { row ->
            row.mapNotNull { square ->
                if(square.piece != null) null
                for(direction in directions){
                    val directionalPositions =
                        generateSequence(Pair(square.row + direction.first, square.col + direction.second)) {
                            Pair(it.first + direction.first, it.second + direction.second)
                        }.takeWhile {
                            it.first in board.indices && it.second in board[it.first].indices
                        }.toList()
                    val opponents = directionalPositions.filter { (x, y) -> board[x][y].piece?.color != currentPlayer.color }
                    if(opponents.isNotEmpty()){
                        val last = opponents.last()
                        val next = Pair(last.first + direction.first, last.second + direction.second)
                        if (next.first in board.indices &&
                            next.second in board[next.first].indices &&
                            board[next.first][next.second].piece?.color == currentPlayer.color){
                            return@mapNotNull square
                        }
                    }
                }
                null
            }
        }.distinct()
        return validSquares
    }
}

