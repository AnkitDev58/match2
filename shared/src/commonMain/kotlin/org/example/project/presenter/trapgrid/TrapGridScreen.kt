package org.example.project.presenter.trapgrid

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.project.ui.GameCard
import org.example.project.ui.ScoreBoard
import org.example.project.ui.SetupScreen

@Composable
fun TrapGridScreen(viewModel: TrapGridViewModel, onNavigateBack: () -> Unit) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                TrapGridEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize().safeContentPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (state.phase == GamePhase.Setup) {
            Box(modifier = Modifier.fillMaxSize()) {
                SetupScreen(
                    title = "Trap Grid Setup",
                    buttonText = "Start Game",
                    onStartGame = { gridSize, contentMode ->
                        viewModel.startGame(gridSize, contentMode)
                    }
                )
                IconButton(
                    onClick = { viewModel.onNavigateBack() },
                    modifier = Modifier.padding(16.dp).align(Alignment.TopStart)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Lounge")
                }
            }
        } else {
            TrapGamePlayScreen(viewModel, state)
        }
    }
}

@Composable
fun TrapGamePlayScreen(viewModel: TrapGridViewModel, state: GameState) {
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
                IconButton(onClick = { viewModel.onNavigateBack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Trap Grid",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
            }
            IconButton(onClick = { viewModel.goToSetup() }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }

        if (state.phase == GamePhase.Playing || state.phase == GamePhase.GameOver) {
            ScoreBoard(
                scoreA = state.scoreA,
                scoreB = state.scoreB,
                currentPlayer = state.turn,
                isGameOver = state.phase == GamePhase.GameOver
            )
        } else {
            // During secret pick or handoff
            Text(
                text = when (state.phase) {
                    GamePhase.SecretA -> "Player A: Pick a Trap"
                    GamePhase.RevealingA -> "Trap Selected!"
                    GamePhase.SecretB -> "Player B: Pick a Trap"
                    GamePhase.RevealingB -> "Trap Selected!"
                    GamePhase.HandoffA -> "Pass to Player B"
                    GamePhase.HandoffB -> "Pass to Player A"
                    else -> ""
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

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
                when (state.phase) {
                    GamePhase.SecretA, GamePhase.RevealingA, GamePhase.SecretB, GamePhase.RevealingB, GamePhase.Playing, GamePhase.GameOver -> {
                        BoardGrid(
                            state = state,
                            onCellClick = { index ->
                                if (state.phase == GamePhase.SecretA || state.phase == GamePhase.SecretB) {
                                    viewModel.dispatch(GameAction.PickSecret(index))
                                } else if (state.phase == GamePhase.Playing) {
                                    viewModel.dispatch(GameAction.RevealCell(index))
                                }
                            }
                        )
                    }
                    GamePhase.HandoffA, GamePhase.HandoffB -> {
                        HandoffContent(
                            message = if (state.phase == GamePhase.HandoffA) "Pass the device to Player B." else "Pass the device to Player A.",
                            onContinue = { viewModel.dispatch(GameAction.ConfirmHandoff) }
                        )
                    }
                    else -> {}
                }
            }
        }

        if (state.message != null) {
            Text(
                state.message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.startGame(state.gridSize, state.contentMode) },
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

@Composable
fun HandoffContent(message: String, onContinue: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(message, fontSize = 24.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onContinue) {
            Text("Continue")
        }
    }
}

@Composable
fun BoardGrid(
    state: GameState,
    onCellClick: (Int) -> Unit
) {
    val isGameOver = state.phase == GamePhase.GameOver
    val isRevealing = state.phase == GamePhase.RevealingA || state.phase == GamePhase.RevealingB

    LazyVerticalGrid(
        columns = GridCells.Fixed(state.gridSize.cols),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.Center,
        userScrollEnabled = false
    ) {
        items(state.cells.size) { index ->
            val cell = state.cells[index]
            val isTrapA = index == state.secretA
            val isTrapB = index == state.secretB
            val isLoserTrap = index == state.loserSecretIndex

            val highlightColor = when {
                isGameOver && isLoserTrap -> Color.Red
                isGameOver && (isTrapA || isTrapB) -> Color.Yellow
                else -> null
            }

            val isFaceUp = when {
                cell.revealed -> true
                isGameOver -> true
                state.phase == GamePhase.RevealingA && isTrapA -> true
                state.phase == GamePhase.RevealingB && isTrapB -> true
                else -> false
            }
            
            GameCard(
                content = cell.content,
                isFaceUp = isFaceUp,
                onClick = { onCellClick(index) },
                modifier = Modifier.aspectRatio(1f),
                highlightColor = highlightColor,
                enabled = !cell.revealed && !isGameOver && !isRevealing
            )
        }
    }
}
