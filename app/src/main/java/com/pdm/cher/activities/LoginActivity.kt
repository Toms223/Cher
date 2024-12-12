package com.pdm.cher.activities


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import com.pdm.cher.screen.LoginScreen

import com.pdm.cher.ui.theme.CherTheme
import com.pdm.cher.viewmodels.FirebaseResult

import com.pdm.cher.viewmodels.UserAuthViewModel

class LoginActivity: ComponentActivity() {
    private val userAuthViewModel = UserAuthViewModel()
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

