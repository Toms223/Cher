package com.pdm.cher.firebase

abstract class FirebaseExceptions(override val message: String): Exception(message) {
    class InvalidEmail: FirebaseExceptions("Email must be of valid type")
    class InvalidPassword: FirebaseExceptions("Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number and one special character")
    class PlayerAlreadyExists: FirebaseExceptions("Player with this email already exists")
    class InvalidCredentials: FirebaseExceptions("Invalid credentials")
    class FailedToRegister : FirebaseExceptions("Could not register player")
    class PlayerNotInGame : FirebaseExceptions("Player is not in game")
    class AlreadyInvitedAPlayer : FirebaseExceptions("Player has already invited another player")
    class PlayerAlreadyInvited : FirebaseExceptions("Player has already been invited")
    class PlayerNoLongerAvailable : FirebaseExceptions("Player is no longer available")
    class InviteNoLongerAvailable : FirebaseExceptions("Invite is no longer available")
    class PlayerAlreadyInLobby : FirebaseExceptions("Player is already in lobby")
    class ImageCouldNotBeLoaded : FirebaseExceptions("Image could not be loaded")
}