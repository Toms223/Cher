package com.pdm.cher.component

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.pdm.cher.data.Player
import java.io.ByteArrayOutputStream
import java.io.InputStream

@Composable
fun UploadImage(uploadImage: (Player, ByteArray, MutableState<ImageBitmap?>) -> Unit, player: Player, bitmap: MutableState<ImageBitmap?>) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val byteArray = getByteArrayFromUri(context, uri) ?: return@rememberLauncherForActivityResult
            uploadImage(player, byteArray, bitmap)
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    if(player.imageURL != "") {
        Button(onClick = {launcher.launch("image/*")}) {
            Text("Change Image")
        }
    } else {
        Button(onClick = {launcher.launch("image/*")}) {
            Text("Image")
        }
    }
}

private fun getByteArrayFromUri(context: Context, uri: Uri): ByteArray? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val byteArrayOutputStream = ByteArrayOutputStream()

        val buffer = ByteArray(1024)
        var length: Int

        while (inputStream?.read(buffer).also { length = it ?: -1 } != -1) {
            byteArrayOutputStream.write(buffer, 0, length)
        }

        inputStream?.close()
        byteArrayOutputStream.toByteArray()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}