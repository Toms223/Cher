package com.pdm.cher.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pdm.cher.R

@Composable
fun Logo() {
    Row {
        Image(
            painter = painterResource(R.drawable.app_logo),
            contentDescription = "Cher Logo",
            modifier = Modifier.size(200.dp).clip(shape = CircleShape)
        )
    }
}