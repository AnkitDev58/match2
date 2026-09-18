package org.example.project.presenter.matchup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.project.ui.GameCard
import org.example.project.ui.ScoreBoard
import org.example.project.ui.SetupScreen

@Composable
fun MatchUpScreen(viewModel: MatchUpViewModel, onNavigateBack: () -> Unit) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                MatchUpEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize().safeContentPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (state.showSetup) {
            Box(modifier = Modifier.fillMaxSize()) {
                SetupScreen(
                    title = "Match Up Setup",
                    buttonText = "Start Match Up!",
                    onStartGame = { gridSize, contentMode ->
                        viewModel.startGame(gridSize, contentMode)
                    }
                )
                IconButton(
                    onClick = { viewModel.navigateBackToHome() },
                    modifier = Modifier.padding(16.dp).align(Alignment.TopStart)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Lounge")
                }
            }
        } else {
            GamePlayScreen(viewModel, state)
        }
    }
}

@Composable
fun GamePlayScreen(viewModel: MatchUpViewModel, state: GameState) {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.navigateBackToHome() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Match Up!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
            }
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
                        GameCard(
                            content = card.content,
                            isFaceUp = card.isFaceUp,
                            isMatched = card.isMatched,
                            matchedBy = card.matchedBy,
                            isFreeTile = card.isFreeTile,
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
