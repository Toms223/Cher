package com.pdm.cher.activities


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pdm.cher.screen.LoginScreen

import com.pdm.cher.ui.theme.CherTheme
import com.pdm.cher.viewmodels.FirebaseResult

import com.pdm.cher.viewmodels.UserAuthViewModel

class LoginActivity: ComponentActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val userAuthViewModel = UserAuthViewModel(firestore, auth, storage)
    override fun onCreate(savedInstanceState: Bundle?) {
        val email = mutableStateOf("")
        val password = mutableStateOf("")
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        setContent{
            CherTheme {
                LoginScreen(::login, email, password)
            }
        }
    }

    private fun login(email: String, password: String) {
        userAuthViewModel.login(email, password) { result ->
            when (result) {
                is FirebaseResult.Success -> {
                    startActivity(Intent(this, PlayerPageActivity::class.java).putExtra("player", result.data))
                }
                is FirebaseResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

