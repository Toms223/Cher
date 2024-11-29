package com.pdm.cher.data

import com.google.firebase.Firebase

data class Player(val username: String = "", val id: Int = 0){
    fun changeUsername(firestore: Firebase, newUsername: String): Player{
        return this.copy(username = newUsername)
    }
}
