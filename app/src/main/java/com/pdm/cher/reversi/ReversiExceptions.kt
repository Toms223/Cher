package com.pdm.cher.reversi

abstract class ReversiException : Exception() {
    class InvalidPosition : ReversiException()
    class NotYourTurn : ReversiException()
}