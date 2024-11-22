package com.pdm.cher.reversi

data class Reversi(val board: List<List<Square>> = makeEmptyBoard(),
                   val currentPlayer: Player = Player(PlayerColor.WHITE)) {
}

fun makeEmptyBoard(): List<List<Square>> {
    return buildList{
        repeat(8){ row ->
            add(buildList{
                repeat(8){ col ->
                    add(Square(row, col, null))
                }
            })
        }
    }
}