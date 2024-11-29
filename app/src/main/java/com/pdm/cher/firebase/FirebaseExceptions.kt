package com.pdm.cher.firebase

abstract class FirebaseExceptions(override val message: String): Exception(message) {
    class FailedToCreatePlayer(override val message: String = "Failed to create player") : FirebaseExceptions(message)
    class FailedToEnterLobby(override val message: String = "Failed to enter lobby") : FirebaseExceptions(message)
    class FailedToPickPlayer(override val message: String = "Failed to pick player") : FirebaseExceptions(message)
    class FailedToCheckParing(override val message: String = "Failed to check paring") : FirebaseExceptions(message)
    class FailedToStartGame(override val message: String = "Failed to start game") : FirebaseExceptions(message)
    class FailedToMakePlay(override val message: String = "Failed to make play") : FirebaseExceptions(message)
    class FailedToGetGame(override val message: String = "Failed to get game") : FirebaseExceptions(message)
    class FailedToConvertToObject(override val message: String = "Failed to convert to object") : FirebaseExceptions(message)
}