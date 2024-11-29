package com.pdm.cher.reversi

fun makeEmptyBoard(): List<List<Square>> {
    return buildList{
        repeat(8){ row ->
            add(buildList{
                repeat(8){ col ->
                    when {
                        row == 3 && col == 3 || row == 4 && col == 4 -> add(Square(row, col, Color.BLACK))
                        row == 3 && col == 4 || row == 4 && col == 3 -> add(Square(row, col, Color.WHITE))
                        else -> add(Square(row, col, null))
                    }
                }
            })
        }
    }
}

fun isBefore(coordinateOne: Pair<Int, Int>, coordinateTwo: Pair<Int, Int>, direction: Pair<Int, Int>): Boolean {
    val dx = coordinateTwo.first - coordinateOne.first
    val dy = coordinateTwo.second - coordinateOne.second

    val dotProduct = dx * direction.first + dy * direction.second

    return dotProduct > 0
}

fun List<Square>.serialize(): String{
    return this.joinToString(separator = "") { square ->
        square.row.toString() + square.col.toString() + when(square.color){
            Color.BLACK -> "B"
            Color.WHITE -> "W"
            else -> " "
        } + ";"
    }.dropLast(1)
}

fun String.toListSquares(): List<Square>{
    val squares = this.split(";")
    return squares.map { square ->
        val row = square[0].toString().toInt()
        val col = square[1].toString().toInt()
        val color = when(square[2]){
            'B' -> Color.BLACK
            'W' -> Color.WHITE
            else -> null
        }
        Square(row, col, color)
    }
}