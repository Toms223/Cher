package com.pdm.cher.reversi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Square(val row: Int = 0, val col: Int = 0, val color: Color? = null) : Parcelable