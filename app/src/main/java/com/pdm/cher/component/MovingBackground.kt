package com.pdm.cher.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.pdm.cher.R

@Composable
fun MovingBackground(content: @Composable () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()

    // Animating the horizontal offset
    val offsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 600f, // Modify this to control the range of movement
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 600f, // Vertical range
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.background), // Replace with your image
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationX = -offsetX
                    translationY = offsetY
                    scaleX *= 2
                    scaleY *= 2
                }
        )
        content()
    }
}