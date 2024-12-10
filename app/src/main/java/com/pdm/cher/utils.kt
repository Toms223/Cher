package com.pdm.cher

import com.pdm.cher.reversi.Color
import com.pdm.cher.reversi.GameRow
import com.pdm.cher.reversi.Square
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
private const val HASHED_PASSWORD_LENGTH = 64

fun makeEmptyBoard(): List<GameRow> {
    return buildList{
        repeat(8){ row ->
            add(GameRow(buildList{
                repeat(8){ col ->
                    when {
                        row == 3 && col == 3 || row == 4 && col == 4 -> add(Square(row, col, Color.BLACK))
                        row == 3 && col == 4 || row == 4 && col == 3 -> add(Square(row, col, Color.WHITE))
                        else -> add(Square(row, col, null))
                    }
                }
            }))
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
        val row = if(square[0] == '-'){
            -(square[1].toString().toInt())
        } else {
            square[0].toString().toInt()
        }
        val col = if(square[2] == '-'){
            -(square[3].toString().toInt())
        } else {
            square[1].toString().toInt()
        }
        val color = when(square.last()){
            'B' -> Color.BLACK
            'W' -> Color.WHITE
            else -> null
        }
        Square(row, col, color)
    }
}

fun String.hash256(): String {
    // Hash using SHA-256
    val digest = MessageDigest.getInstance("SHA-256")
    val encodedhash = digest.digest(
        this.toByteArray(StandardCharsets.UTF_8)
    )

    // Convert to hexadecimal
    val sb = StringBuilder()
    for (b in encodedhash) {
        val hex = Integer.toHexString(b.toInt())
        if (hex.length == 1) {
            sb.append('0')
        }
        sb.append(hex)
    }

    return sb.toString().substring(0 until HASHED_PASSWORD_LENGTH)
}