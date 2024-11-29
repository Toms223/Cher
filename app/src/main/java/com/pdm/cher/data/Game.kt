package com.pdm.cher.data

import com.pdm.cher.reversi.Reversi

data class Game(val playerBlackId: Int = 0, val playerWhiteId: Int = 0, val reversi: Reversi = Reversi())