package com.example.everise.ui.screens.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.everise.ui.theme.*

@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit,
    onLogin: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EveriseBackgroundLight)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Top Row: App Brand & Page Indicator Pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Surface(
                    shape = CircleShape,
                    color = EverisePaleBlueSurface,
                    modifier = Modifier.size(28.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Outlined.TrendingUp,
                            contentDescription = null,
                            tint = EveriseDeepBlue,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
                Text(
                    text = "Atoms & Habits",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = EverisePrimaryTextLight,
                )
            }

            // Top right page indicator dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(EveriseBrandBlue),
                )
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(EverisePaleBlueSurface),
                )
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(EverisePaleBlueSurface),
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Central Orbital Art Graphic with +1% Daily & Floating Tags
        Box(
            modifier = Modifier
                .size(220.dp),
            contentAlignment = Alignment.Center,
        ) {
            // Dashed orbital background circle and arc
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = size.minDimension / 2.3f

                // Dashed outer ring
                drawCircle(
                    color = Color(0xFFD4E3ED),
                    radius = radius,
                    center = center,
                    style = Stroke(
                        width = 1.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f),
                    ),
                )

                // Bottom solid arc
                drawArc(
                    color = Color(0xFF5B9FC5),
                    startAngle = 30f,
                    sweepAngle = 140f,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2f, radius * 2f),
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round),
                )
            }

            // Central +1% DAILY circle
            Surface(
                shape = CircleShape,
                color = EverisePaleBlueSurface,
                modifier = Modifier.size(105.dp),
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Surface(
                        shape = CircleShape,
                        color = EveriseBrandBlue,
                        modifier = Modifier.size(56.dp),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = "+1%",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                            )
                            Text(
                                text = "DAILY",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.9f),
                            )
                        }
                    }
                }
            }

            // Top-right floating "Mindful" tag
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = EveriseWhiteSurface,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = 14.dp),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        Icons.Outlined.Spa,
                        contentDescription = null,
                        tint = EveriseSageGreen,
                        modifier = Modifier.size(14.dp),
                    )
                    Text(
                        "Mindful",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = EverisePrimaryTextLight,
                    )
                }
            }

            // Bottom-left floating "Effortless" tag
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = EveriseWhiteSurface,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = (-6).dp, y = (-10).dp),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        Icons.Outlined.Autorenew,
                        contentDescription = null,
                        tint = EveriseDeepBlue,
                        modifier = Modifier.size(14.dp),
                    )
                    Text(
                        "Effortless",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = EverisePrimaryTextLight,
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Headline & Subtitle
        Text(
            text = "Small habits.\nBig changes.",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = EverisePrimaryTextLight,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Build routines that fit your natural daily rhythm. Backed by proven behavioral psychology, crafted for calm focus.",
            style = MaterialTheme.typography.bodyMedium,
            color = EveriseSecondaryTextLight,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp),
            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight,
        )

        Spacer(Modifier.height(22.dp))

        // 3 Feature Cards matching Stitch
        FeatureCard(
            icon = Icons.Outlined.Widgets,
            iconTint = EveriseDeepBlue,
            iconBg = EverisePaleBlueSurface,
            title = "Atomic Consistency",
            subtitle = "Micro-steps that quietly compound over time",
        )
        Spacer(Modifier.height(10.dp))
        FeatureCard(
            icon = Icons.Outlined.CalendarMonth,
            iconTint = Color(0xFF285C38),
            iconBg = EverisePaleSageSurface,
            title = "Intelligent Heatmaps",
            subtitle = "Visualize your steady momentum without guilt",
        )
        Spacer(Modifier.height(10.dp))
        FeatureCard(
            icon = Icons.Outlined.SelfImprovement,
            iconTint = EveriseDeepBlue,
            iconBg = EverisePaleBlueSurface,
            title = "Calm Focus",
            subtitle = "Distraction-free tracking without aggressive streaks",
        )

        Spacer(Modifier.height(24.dp))

        // Action Buttons: Get Started -> and I already have an account
        Button(
            onClick = onGetStarted,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(27.dp),
            colors = ButtonDefaults.buttonColors(containerColor = EveriseBrandBlue),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Get Started",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        TextButton(onClick = onLogin) {
            Text(
                text = "I already have an account",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = EveriseDeepBlue,
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun FeatureCard(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    subtitle: String,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = EveriseWhiteSurface,
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp),
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = EverisePrimaryTextLight,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = EveriseSecondaryTextLight,
                )
            }
        }
    }
}
