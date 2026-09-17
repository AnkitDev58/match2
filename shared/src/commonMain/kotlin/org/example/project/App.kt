package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.project.logic.MemoryGameViewModel
import org.example.project.ui.CardComposable
import org.example.project.ui.ScoreBoard
import org.example.project.ui.SetupScreen
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val viewModel: MemoryGameViewModel = viewModel { MemoryGameViewModel() }
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        Surface(
            modifier = Modifier.fillMaxSize().safeContentPadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (state.showSetup) {
                SetupScreen(
                    onStartGame = { gridSize, contentMode ->
                        viewModel.startGame(gridSize, contentMode)
                    }
                )
            } else {
                GameScreen(viewModel, state)
            }
        }
    }
}

@Composable
fun GameScreen(viewModel: MemoryGameViewModel, state: org.example.project.model.GameState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Match Up!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
            IconButton(onClick = { viewModel.goToSetup() }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }

        ScoreBoard(
            scoreA = state.scoreA,
            scoreB = state.scoreB,
            currentPlayer = state.currentPlayer,
            isGameOver = state.isGameOver
        )

        Spacer(modifier = Modifier.height(16.dp))

        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            val boardSize = minOf(maxWidth, maxHeight)
            Box(
                modifier = Modifier.size(boardSize)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(state.gridSize.cols),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(4.dp),
                    verticalArrangement = Arrangement.Center,
                    userScrollEnabled = false
                ) {
                    items(state.cards) { card ->
                        CardComposable(
                            card = card,
                            onClick = { viewModel.onCardClicked(card.id) },
                            modifier = Modifier.aspectRatio(1f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.newGame() },
                modifier = Modifier.weight(1f).height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Restart", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            OutlinedButton(
                onClick = { viewModel.goToSetup() },
                modifier = Modifier.weight(1f).height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Settings", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
