package com.pdm.cher.activities


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import com.pdm.cher.screen.RegisterScreen
import com.pdm.cher.ui.theme.CherTheme
import com.pdm.cher.viewmodels.FirebaseResult
import com.pdm.cher.viewmodels.UserAuthViewModel

class RegisterActivity: ComponentActivity() {
    private val userAuthViewModel = UserAuthViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        val username = mutableStateOf("")
        val email = mutableStateOf("")
        val checkEmail = mutableStateOf("")
        val password = mutableStateOf("")
        val checkPassword = mutableStateOf("")
        super.onCreate(savedInstanceState)
        setContent{
            CherTheme {
                RegisterScreen(::register, username, email, checkEmail, password, checkPassword)
            }
        }
    }

    private fun register(username: String, email: String, checkEmail: String, password: String, checkPassword: String){
        if (email == checkEmail && password == checkPassword){
            userAuthViewModel.register(username, email, password){
                when(it){
                    is FirebaseResult.Success -> {
                        startActivity(Intent(this, PlayerPageActivity::class.java).putExtra("player", it.data))
                    }
                    is FirebaseResult.Error -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

