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
import com.pdm.cher.screen.RegisterScreen
import com.pdm.cher.ui.theme.CherTheme
import com.pdm.cher.viewmodels.FirebaseResult
import com.pdm.cher.viewmodels.UserAuthViewModel

class RegisterActivity: ComponentActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val userAuthViewModel = UserAuthViewModel(firestore, auth, storage)
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
                        startActivity(Intent(this, LoginActivity::class.java).putExtra("player", it.data))
                    }
                    is FirebaseResult.Error -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

