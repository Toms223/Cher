package com.pdm.cher.reversi

abstract class ReversiException(override val message: String? = null) : Exception() {
    class InvalidPosition(override val message: String = "Invalid Position") : ReversiException(message)
    class NotYourTurn(override val message: String = "Not Your Turn") : ReversiException(message)
}