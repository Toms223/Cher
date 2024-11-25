package com.pdm.cher.reversi

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