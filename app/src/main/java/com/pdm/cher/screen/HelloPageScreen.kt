package com.pdm.cher.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pdm.cher.R
import com.pdm.cher.activities.LoginActivity
import com.pdm.cher.activities.RegisterActivity
import com.pdm.cher.component.MovingBackground

@Preview(showBackground = true)
@Composable
fun HelloPageScreen(context: Context) {
    MovingBackground {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize())
        {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.app_logo),
                    contentDescription = "Cher Logo",
                    modifier = Modifier.size(200.dp).clip(shape = CircleShape)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    context.startActivity(Intent(context, LoginActivity::class.java))
                }) {
                    Text("Login")
                }
                Button(onClick = {
                    context.startActivity(Intent(context, RegisterActivity::class.java))
                }) {
                    Text("Register")
                }
            }
        }
    }
}
