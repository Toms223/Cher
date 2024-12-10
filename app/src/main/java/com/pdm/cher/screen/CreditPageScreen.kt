package com.pdm.cher.screen

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.pdm.cher.R
import com.pdm.cher.ui.theme.primaryContainerLight

@Composable
fun CreditCard() {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text(fontWeight = FontWeight.Bold , fontSize = 10.em, text = "Developed by:")
        }
        Row(horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Developer("Tomás", painterResource(R.drawable.default_avatar))
            Developer("Felipe", painterResource(R.drawable.default_avatar))
            Developer("Antonio", painterResource(R.drawable.default_avatar))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(fontWeight = FontWeight.Bold, fontSize = 6.em, text ="Contact Us")
            SendEmail()
        }
    }
}

@Composable
fun Developer(name: String, painter: Painter){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(fontSize = 5.em, text=name)
        Image(painter = painter, contentDescription = "Developer Image", modifier = Modifier.size(96.dp), colorFilter = ColorFilter.tint(
            primaryContainerLight
        ))
    }
}

@Composable
fun SendEmail(){
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { //Do something when user comes back in app
        })
    Button(onClick = {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(Intent.EXTRA_EMAIL, arrayOf("A42146@alunos.isel.pt","A49427@alunos.isel.pt","A49478@alunos.isel.pt"))
            putExtra(Intent.EXTRA_SUBJECT, "Opinion on CHER") // Email subject
        }
        launcher.launch(intent)
    }) {
        Text("Send your opinion!")
    }
}