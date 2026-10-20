package com.example.everise.ui.screens.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.everise.ui.theme.EveriseTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinished: () -> Unit,
) {
    var startAnim by remember { mutableFloatStateOf(0f) }
    val alpha by animateFloatAsState(
        targetValue = startAnim,
        animationSpec = tween(800),
        label = "splashAlpha",
    )
    val scale by animateFloatAsState(
        targetValue = if (startAnim > 0f) 1f else 0.85f,
        animationSpec = tween(800),
        label = "splashScale",
    )

    LaunchedEffect(Unit) {
        startAnim = 1f
        delay(1600)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .alpha(alpha)
                .scale(scale),
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "+1%",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Everise",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Improve a little every day",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashPreview() {
    EveriseTheme {
        SplashScreen(onFinished = {})
    }
}
