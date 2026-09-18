package org.example.project.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.model.CardContent
import org.example.project.model.Player

@Composable
fun GameCard(
    content: CardContent,
    isFaceUp: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isMatched: Boolean = false,
    matchedBy: Player? = null,
    isFreeTile: Boolean = false,
    highlightColor: Color? = null,
    enabled: Boolean = true
) {
    val playerAColor = Color(0xFF4285F4)
    val playerBColor = Color(0xFFEA4335)

    val rotation by animateFloatAsState(
        targetValue = if (isFaceUp || isMatched) 180f else 0f,
        animationSpec = tween(durationMillis = 400)
    )

    val cardModifier = when {
        isFreeTile -> {
            modifier
                .padding(4.dp)
                .border(2.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(8.dp))
        }
        isMatched && matchedBy != null -> {
            modifier
                .padding(4.dp)
                .border(
                    width = 2.dp,
                    color = if (matchedBy == Player.A) playerAColor else playerBColor,
                    shape = RoundedCornerShape(8.dp)
                )
        }
        highlightColor != null -> {
            modifier
                .padding(4.dp)
                .border(2.dp, highlightColor, RoundedCornerShape(8.dp))
                .clickable(enabled = enabled, onClick = onClick)
        }
        else -> {
            modifier
                .padding(4.dp)
                .clickable(
                    enabled = enabled && !isFaceUp && !isMatched,
                    onClick = onClick
                )
        }
    }

    Card(
        modifier = cardModifier
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isFreeTile) 0.dp else 4.dp),
        colors = when {
            isFreeTile -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            highlightColor != null -> CardDefaults.cardColors(containerColor = highlightColor.copy(alpha = 0.2f))
            isMatched && matchedBy == Player.A -> CardDefaults.cardColors(containerColor = playerAColor.copy(alpha = 0.2f))
            isMatched && matchedBy == Player.B -> CardDefaults.cardColors(containerColor = playerBColor.copy(alpha = 0.2f))
            else -> CardDefaults.cardColors()
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (rotation > 90f) {
                        when {
                            isFreeTile -> MaterialTheme.colorScheme.secondaryContainer
                            highlightColor != null -> highlightColor.copy(alpha = 0.2f)
                            isMatched && matchedBy == Player.A -> playerAColor.copy(alpha = 0.2f)
                            isMatched && matchedBy == Player.B -> playerBColor.copy(alpha = 0.2f)
                            else -> MaterialTheme.colorScheme.surface
                        }
                    } else MaterialTheme.colorScheme.primary
                ),
            contentAlignment = Alignment.Center
        ) {
            if (rotation > 90f) {
                Box(
                    modifier = Modifier.fillMaxSize().graphicsLayer { rotationY = 180f },
                    contentAlignment = Alignment.Center
                ) {
                    when (content) {
                        is CardContent.Emoji -> {
                            Text(
                                text = if (isFreeTile) "⭐" else content.value,
                                fontSize = 32.sp
                            )
                        }
                        is CardContent.Number -> {
                            Text(
                                text = content.value.toString(),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        is CardContent.Color -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                                    .background(content.value, RoundedCornerShape(4.dp))
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "?",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 32.sp
                )
            }
        }
    }
}
