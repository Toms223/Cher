package com.pdm.cher.reversi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameRow(val list: List<Square> = emptyList()): Parcelable