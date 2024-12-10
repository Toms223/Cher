package com.pdm.cher.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.pdm.cher.component.Logo

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RegisterScreen(
    onRegister: (String, String, String, String, String) -> Unit,
    username: MutableState<String>,
    email: MutableState<String>,
    checkEmail: MutableState<String>,
    password: MutableState<String>,
    checkPassword: MutableState<String>
    )
{
    val rememberUsername by remember { mutableStateOf(username) }
    val rememberEmail by remember { mutableStateOf(email) }
    val rememberCheckEmail by remember { mutableStateOf(checkEmail) }
    val rememberPassword by remember { mutableStateOf(password) }
    val rememberCheckPassword by remember { mutableStateOf(checkPassword) }
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)){
                    Icon(imageVector = Icons.Filled.Info, "Credits")
                    Text("Welcome to Cher")
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
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(value = rememberUsername.value, onValueChange = { rememberUsername.value = it  }, label = { Text("Username") })
                TextField(value = rememberEmail.value, onValueChange = { rememberEmail.value = it  }, label = { Text("Email") })
                TextField(value = rememberCheckEmail.value, onValueChange = { rememberCheckEmail.value = it  }, label = { Text("Re-enter Email") })
                TextField(value = rememberPassword.value, onValueChange = { rememberPassword.value = it  }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
                TextField(value = rememberCheckPassword.value, onValueChange = { rememberCheckPassword.value = it  }, label = { Text("Re-enter Password") }, visualTransformation = PasswordVisualTransformation())
            }
            Button(onClick = {
                onRegister(rememberUsername.value, rememberEmail.value, rememberCheckEmail.value, rememberPassword.value, rememberCheckPassword.value)
            }) {
                Text("Login")
            }
        }
    }
}