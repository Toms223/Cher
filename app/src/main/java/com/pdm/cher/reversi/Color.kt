package com.pdm.cher.reversi

enum class Color {
    BLACK,
    WHITE;

    fun opposite(): Color {
        return when (this) {
            BLACK -> WHITE
            WHITE -> BLACK
        }
    }
}