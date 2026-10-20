package com.example.everise.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.everise.ui.theme.EveriseTheme
import kotlinx.coroutines.launch

private data class OnboardingPage(
    val title: String,
    val body: String,
    val badge: String,
)

private val pages = listOf(
    OnboardingPage(
        title = "Small habits, big results",
        body = "Improve just 1% each day. Over a year, that compounds into something remarkable.",
        badge = "+1%",
    ),
    OnboardingPage(
        title = "Make it obvious & easy",
        body = "Clear cues, gentle reminders, and one-tap check-ins so building habits feels effortless.",
        badge = "Cue",
    ),
    OnboardingPage(
        title = "Stay consistent, not perfect",
        body = "Miss a day? Simply begin again. Everise celebrates progress without guilt.",
        badge = "Restart",
    ),
)

@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit,
    onLogin: () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 32.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            TextButton(onClick = onGetStarted) {
                Text(
                    text = "Skip",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) { page ->
            OnboardingPageContent(pages[page])
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(pages.size) { index ->
                val selected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (selected) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant,
                        ),
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (pagerState.currentPage < pages.lastIndex) {
            Button(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            ) {
                Text("Continue", style = MaterialTheme.typography.labelLarge)
            }
        } else {
            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            ) {
                Text("Get started", style = MaterialTheme.typography.labelLarge)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onLogin,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(999.dp),
        ) {
            Text(
                "I already have an account",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = page.badge,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = page.body,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(300.dp),
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun OnboardingPreview() {
    EveriseTheme {
        OnboardingScreen(onGetStarted = {}, onLogin = {})
    }
}
