package org.example.project.presenter.trapgrid

import androidx.compose.ui.graphics.Color
import org.example.project.model.*

enum class GamePhase {
    Setup, SecretA, RevealingA, HandoffA, SecretB, RevealingB, HandoffB, Playing, GameOver
}

data class Cell(
    val content: CardContent,
    val revealed: Boolean = false
)

data class GameState(
    val phase: GamePhase = GamePhase.Setup,
    val gridSize: GridSize = GridSize.FOUR_BY_FOUR,
    val contentMode: ContentMode = ContentMode.EMOJI,
    val cells: List<Cell> = emptyList(),
    val secretA: Int? = null,
    val secretB: Int? = null,
    val turn: Player = Player.A,
    val scoreA: Int = 0,
    val scoreB: Int = 0,
    val loser: Player? = null,
    val loserSecretIndex: Int? = null,
    val message: String? = null
)

sealed class GameAction {
    data class SelectSize(val size: GridSize, val contentMode: ContentMode) : GameAction()
    data class PickSecret(val index: Int) : GameAction()
    data object ConfirmSecretReveal : GameAction()
    data object ConfirmHandoff : GameAction()
    data class RevealCell(val index: Int) : GameAction()
    data object NewGame : GameAction()
}

private val emojiPool = listOf(
    "🍎", "🍌", "🍇", "🍓", "🍒", "🍑", "🍍", "🥝", "🥥", "🍋", "🍊", "🍉", "🍈", "🍐", "🫐", "🥭", "🥑", "🥦",
    "🍄", "🌻", "🌈", "🍦", "🍕", "🍔", "🍟", "🍩", "🍪", "🎂", "🎸", "⚽", "🏀", "🚀", "🛸", "💎", "🔥", "✨"
)

private val colorPool = listOf(
    Color(0xFFF44336), Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF673AB7),
    Color(0xFF3F51B5), Color(0xFF2196F3), Color(0xFF03A9F4), Color(0xFF00BCD4),
    Color(0xFF009688), Color(0xFF4CAF50), Color(0xFF8BC34A), Color(0xFFCDDC39),
    Color(0xFFFFEB3B), Color(0xFFFFC107), Color(0xFFFF9800), Color(0xFFFF5722),
    Color(0xFF795548), Color(0xFF9E9E9E),
    Color(0xFF000000), Color(0xFF808080), Color(0xFF800000), Color(0xFF808000), Color(0xFF008000), Color(0xFF800080), Color(0xFF008080), Color(0xFF000080),
    Color(0xFFA52A2A), Color(0xFFFF7F50), Color(0xFFD2691E), Color(0xFFFFD700), Color(0xFFADFF2F), Color(0xFF4B0082), Color(0xFFF0E68C), Color(0xFFB0C4DE), Color(0xFFFF00FF), Color(0xFF00FF00)
)

fun reduce(state: GameState, action: GameAction): GameState {
    return when (action) {
        is GameAction.SelectSize -> {
            val totalCells = action.size.rows * action.size.cols
            val contents = when (action.contentMode) {
                ContentMode.EMOJI -> emojiPool.shuffled().take(totalCells).map { CardContent.Emoji(it) }
                ContentMode.NUMBER -> (1..totalCells).toList().shuffled().map { CardContent.Number(it) }
                ContentMode.COLOR -> colorPool.shuffled().take(totalCells).map { CardContent.Color(it) }
            }
            state.copy(
                phase = GamePhase.SecretA,
                gridSize = action.size,
                contentMode = action.contentMode,
                cells = contents.map { Cell(it) },
                secretA = null,
                secretB = null,
                turn = Player.A,
                loser = null,
                loserSecretIndex = null,
                message = null,
                scoreA = 0,
                scoreB = 0
            )
        }
        is GameAction.PickSecret -> {
            when (state.phase) {
                GamePhase.SecretA -> {
                    state.copy(
                        secretA = action.index,
                        phase = GamePhase.RevealingA
                    )
                }
                GamePhase.SecretB -> {
                    if (action.index == state.secretA) {
                        state.copy(
                            phase = GamePhase.GameOver,
                            loser = Player.B,
                            loserSecretIndex = action.index,
                            scoreA = 1,
                            scoreB = 0,
                            cells = state.cells.map { it.copy(revealed = true) }
                        )
                    } else {
                        state.copy(
                            secretB = action.index,
                            phase = GamePhase.RevealingB,
                            message = null
                        )
                    }
                }
                else -> state
            }
        }
        GameAction.ConfirmSecretReveal -> {
            when (state.phase) {
                GamePhase.RevealingA -> state.copy(phase = GamePhase.HandoffA)
                GamePhase.RevealingB -> state.copy(phase = GamePhase.HandoffB)
                else -> state
            }
        }
        GameAction.ConfirmHandoff -> {
            when (state.phase) {
                GamePhase.HandoffA -> state.copy(phase = GamePhase.SecretB)
                GamePhase.HandoffB -> state.copy(phase = GamePhase.Playing)
                else -> state
            }
        }
        is GameAction.RevealCell -> {
            if (state.phase != GamePhase.Playing) return state
            val index = action.index
            if (state.cells[index].revealed) return state

            val newCells = state.cells.mapIndexed { i, cell ->
                if (i == index) cell.copy(revealed = true) else cell
            }

            val opponentTrap = if (state.turn == Player.A) state.secretB else state.secretA

            // Loss check
            if (index == opponentTrap) {
                return state.copy(
                    cells = newCells.map { it.copy(revealed = true) },
                    phase = GamePhase.GameOver,
                    loser = state.turn,
                    loserSecretIndex = index,
                    scoreA = if (state.turn == Player.B) 1 else 0,
                    scoreB = if (state.turn == Player.A) 1 else 0
                )
            }

            // Tie check (primary): if only secretA and secretB remain hidden
            val unrevealedIndices = newCells.mapIndexedNotNull { i, cell ->
                if (!cell.revealed) i else null
            }.toSet()

            if (unrevealedIndices == setOf(state.secretA, state.secretB)) {
                return state.copy(
                    cells = newCells.map { it.copy(revealed = true) },
                    phase = GamePhase.GameOver,
                    scoreA = 0,
                    scoreB = 0
                )
            }

            // Tie check (fallback): zero unrevealed cells remain
            if (unrevealedIndices.isEmpty()) {
                return state.copy(
                    cells = newCells.map { it.copy(revealed = true) },
                    phase = GamePhase.GameOver,
                    scoreA = 0,
                    scoreB = 0
                )
            }

            // Otherwise: switch turn
            state.copy(
                cells = newCells,
                turn = if (state.turn == Player.A) Player.B else Player.A
            )
        }
        GameAction.NewGame -> {
            GameState()
        }
    }
}
