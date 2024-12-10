package com.pdm.cher.data

import android.os.Parcelable
import com.pdm.cher.reversi.Color
import com.pdm.cher.reversi.ReversiRepetition
import kotlinx.parcelize.Parcelize


@Parcelize
data class GameRepetition(val opponentColor: Color = Color.BLACK, val opponentEmail: String = "", val reversiRepetition: ReversiRepetition = ReversiRepetition()): Parcelable