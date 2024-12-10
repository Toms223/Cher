package com.pdm.cher.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pdm.cher.component.Logo

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    email: MutableState<String>,
    password: MutableState<String>
) {
    val rememberEmail = remember { email }
    val rememberPassword = remember { password }
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()){
                    Icon(imageVector = Icons.Filled.AccountCircle, "Credits")
                    Text("Login")
                }
            }
        )
    }) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            Logo()
            Spacer(modifier = Modifier.height(16.dp))
            TextField(value = rememberEmail.value, onValueChange = { rememberEmail.value = it  }, label = { Text("Email") })
            TextField(value = rememberPassword.value, onValueChange = { rememberPassword.value = it  }, label = { Text("Password") })
            Button(onClick = {
                onLogin(rememberEmail.value, rememberPassword.value)
            }) {
                Text("Login")
            }
        }
    }
}