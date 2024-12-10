package com.pdm.cher


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pdm.cher.activities.PlayerPageActivity
import com.pdm.cher.ui.theme.CherTheme
import com.pdm.cher.screen.HelloPageScreen
import com.pdm.cher.viewmodels.FirebaseResult
import com.pdm.cher.viewmodels.PlayerInformationViewModel

class MainActivity: ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()
        val playerInformationViewModel = PlayerInformationViewModel(firestore, auth, storage)
        super.onCreate(savedInstanceState)
        val user = auth.currentUser
        if (user != null && user.email != null) {
            playerInformationViewModel.getPlayer(user.email!!) { result ->
                if(result is FirebaseResult.Success) {
                    startActivity(Intent(this, PlayerPageActivity::class.java).putExtra("player", result.data))
                    finish()
                } else {
                    setContent {
                        CherTheme {
                            HelloPageScreen(this)
                        }
                    }
                }
            }
        }
        actionBar?.hide()
        if(user == null) {
            setContent {
                CherTheme {
                    HelloPageScreen(this)
                }
            }
        }
    }
}



