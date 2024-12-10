package com.pdm.cher.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pdm.cher.data.Player
import com.pdm.cher.firebase.ReversiFirebase
import kotlinx.coroutines.launch

class UserAuthViewModel(firebase: FirebaseFirestore, auth: FirebaseAuth, storage: FirebaseStorage): ViewModel() {
    private val reversiFirebase = ReversiFirebase(firebase, auth, storage)

    fun login(username: String, password: String, onResult: (FirebaseResult<Player>) -> Unit) = viewModelScope.launch {
        try {
            onResult(FirebaseResult.Success(reversiFirebase.login(username, password)))
        } catch (e: Exception) {
            onResult(FirebaseResult.Error(e.message))
        }

    }

    fun register(username: String, email: String, password: String, onResult: (FirebaseResult<Player>) -> Unit) = viewModelScope.launch {
        try {
            onResult(FirebaseResult.Success(reversiFirebase.register(username, email, password)))
        } catch (e: Exception) {
            onResult(FirebaseResult.Error(e.message))
        }
    }
}