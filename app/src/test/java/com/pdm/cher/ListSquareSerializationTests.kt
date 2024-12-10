package com.pdm.cher

import com.pdm.cher.reversi.Color
import com.pdm.cher.reversi.Square
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class ListSquareSerializationTests {
    @Test
    fun `Should serialize list of squares`() {
        val squares = listOf(
            Square(1, 2, Color.BLACK),
            Square(3, 4, Color.WHITE),
            Square(5, 6, null),
            Square(-1, -1, null)
        )

        val serialized = squares.serialize()

        assertEquals("12B;34W;56 ;-1-1 ", serialized)
    }

    @Test
    fun `Should deserialize list of squares`() {
        val serialized = "12B;34W;56 ;-1-1 "
        val squares = serialized.toListSquares()

        assertIterableEquals(
            listOf(
                Square(1, 2, Color.BLACK),
                Square(3, 4, Color.WHITE),
                Square(5, 6, null),
                Square(-1, -1, null)
            ),
            squares
        )
    }
}