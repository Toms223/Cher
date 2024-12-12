package com.pdm.cher.data

import android.os.Parcelable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Player(
    val username: String = "",
    val email : String = "",
    val randomId : String = "#${(0..1000).random()}",
    val wins : Int = 0,
    val losses: Int = 0,
    val imageURL : String = ""
) : Parcelable {


    @Composable
    fun getImage(bitmapState: MutableState<ImageBitmap?>, getImage: (String, MutableState<ImageBitmap?>) -> Unit, size: Dp = 200.dp) {
        val image by remember { bitmapState }
        LaunchedEffect(imageURL) {
            if (imageURL != "") {
                getImage(email, bitmapState)
            }
        }
        if (image != null) {
            Image(bitmap = image!!, contentDescription = "Player Image", modifier = Modifier.clip(CircleShape).size(size))
        } else {
            if(imageURL != "") {
                CircularProgressIndicator()
            } else {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Player Image", modifier = Modifier.size(size))
            }
        }
    }
}
