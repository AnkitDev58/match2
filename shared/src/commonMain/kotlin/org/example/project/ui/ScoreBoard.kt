package org.example.project.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.model.Player

@Composable
fun ScoreBoard(
    scoreA: Int,
    scoreB: Int,
    currentPlayer: Player,
    isGameOver: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PlayerScore(
                name = "Player A",
                score = scoreA,
                isActive = currentPlayer == Player.A && !isGameOver,
                color = Color(0xFF4285F4)
            )
            PlayerScore(
                name = "Player B",
                score = scoreB,
                isActive = currentPlayer == Player.B && !isGameOver,
                color = Color(0xFFEA4335)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = when {
                isGameOver -> {
                    when {
                        scoreA > scoreB -> "Player A Wins!"
                        scoreB > scoreA -> "Player B Wins!"
                        else -> "It's a Tie!"
                    }
                }
                currentPlayer == Player.A -> "Player A's Turn"
                else -> "Player B's Turn"
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun PlayerScore(
    name: String,
    score: Int,
    isActive: Boolean,
    color: Color
) {
    Surface(
        modifier = Modifier
            .width(120.dp)
            .border(
                width = if (isActive) 3.dp else 1.dp,
                color = if (isActive) color else Color.Gray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        tonalElevation = if (isActive) 8.dp else 0.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = name, fontSize = 14.sp)
            Text(text = score.toString(), fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = color)
        }
    }
}
