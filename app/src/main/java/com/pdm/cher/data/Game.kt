package com.pdm.cher.data

import android.os.Parcelable
import com.pdm.cher.reversi.Reversi
import kotlinx.parcelize.Parcelize

@Parcelize
data class Game(
    val playerBlackEmail: String = "",
    val playerWhiteEmail: String = "",
    val reversi: Reversi = Reversi(),
    val id : String = "#${(0..1000000).random()}"
): Parcelable