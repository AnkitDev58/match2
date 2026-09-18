package org.example.project.presenter.matchup

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.base.BaseViewModel
import org.example.project.model.*


class MatchUpViewModel : BaseViewModel<GameState, MatchUpEvent>(GameState()) {

    private val MATCH_DELAY = 800L

    private val emojiPool = listOf(
        "🍎", "🍌", "🍇", "🍓", "🍒", "🍑", "🍍", "🥝", 
        "🥥", "🍋", "🍊", "🍉", "🍈", "🍐", "🫐", "🥭", "🥑", "🥦"
    )

    private val colorPool = listOf(
        Color(0xFFF44336),
        Color(0xFFE91E63),
        Color(0xFF9C27B0),
        Color(0xFF673AB7),
        Color(0xFF3F51B5),
        Color(0xFF2196F3),
        Color(0xFF03A9F4),
        Color(0xFF00BCD4),
        Color(0xFF009688),
        Color(0xFF4CAF50),
        Color(0xFF8BC34A),
        Color(0xFFCDDC39),
        Color(0xFFFFEB3B),
        Color(0xFFFFC107),
        Color(0xFFFF9800),
        Color(0xFFFF5722),
        Color(0xFF795548),
        Color(0xFF9E9E9E)
    )

    fun startGame(gridSize: GridSize, contentMode: ContentMode) {
        val pairsNeeded = gridSize.pairsNeeded
        val contentList = when (contentMode) {
            ContentMode.EMOJI -> emojiPool.take(pairsNeeded).map { CardContent.Emoji(it) }
            ContentMode.NUMBER -> (1..pairsNeeded).map { CardContent.Number(it) }
            ContentMode.COLOR -> colorPool.take(pairsNeeded).map { CardContent.Color(it) }
        }

        val fullContent = (contentList + contentList).shuffled()
        val cards = mutableListOf<CardState>()
        
        var contentIndex = 0
        for (i in 0 until gridSize.totalCells) {
            if (gridSize == GridSize.FIVE_BY_FIVE && i == 12) {
                cards.add(
                    CardState(
                        id = i,
                        content = CardContent.Emoji("⭐"), // Placeholder for free tile
                        isFaceUp = true,
                        isMatched = true,
                        isFreeTile = true
                    )
                )
            } else {
                cards.add(
                    CardState(
                        id = i,
                        content = fullContent[contentIndex]
                    )
                )
                contentIndex++
            }
        }

        updateState {
            it.copy(
                gridSize = gridSize,
                contentMode = contentMode,
                cards = cards,
                showSetup = false,
                flippedIds = emptyList(),
                inputLocked = false,
                isGameOver = false,
                scoreA = 0,
                scoreB = 0,
                currentPlayer = Player.A
            )
        }
    }

    fun goToSetup() {
        updateState { it.copy(showSetup = true) }
    }

    fun navigateBackToHome() {
        sendEvent(MatchUpEvent.NavigateBack)
    }

    fun newGame() {
        val state = currentState
        startGame(state.gridSize, state.contentMode)
    }

    fun onCardClicked(id: Int) {
        val state = currentState
        if (state.inputLocked || state.isGameOver) return

        val card = state.cards.find { it.id == id } ?: return
        if (card.isFaceUp || card.isMatched || card.isFreeTile) return

        val updatedCards = state.cards.map {
            if (it.id == id) it.copy(isFaceUp = true) else it
        }
        
        val newFlippedIds = state.flippedIds + id

        updateState { 
            it.copy(
                cards = updatedCards,
                flippedIds = newFlippedIds
            )
        }

        if (newFlippedIds.size == 2) {
            resolveTurn(newFlippedIds)
        }
    }

    private fun resolveTurn(flippedIds: List<Int>) {
        updateState { it.copy(inputLocked = true) }

        viewModelScope.launch {
            delay(MATCH_DELAY)
            
            val state = currentState
            val firstCard = state.cards.find { it.id == flippedIds[0] }!!
            val secondCard = state.cards.find { it.id == flippedIds[1] }!!

            if (isSameContent(firstCard.content, secondCard.content)) {
                // Match
                val updatedCards = state.cards.map {
                    if (it.id == flippedIds[0] || it.id == flippedIds[1]) {
                        it.copy(isMatched = true, matchedBy = state.currentPlayer)
                    } else {
                        it
                    }
                }
                
                val newScoreA = if (state.currentPlayer == Player.A) state.scoreA + 1 else state.scoreA
                val newScoreB = if (state.currentPlayer == Player.B) state.scoreB + 1 else state.scoreB
                
                val matchedCount = updatedCards.count { it.isMatched && !it.isFreeTile }
                val totalPairsCount = state.gridSize.pairsNeeded * 2
                val isGameOver = matchedCount == totalPairsCount

                updateState {
                    it.copy(
                        cards = updatedCards,
                        scoreA = newScoreA,
                        scoreB = newScoreB,
                        flippedIds = emptyList(),
                        inputLocked = false,
                        isGameOver = isGameOver
                    )
                }
            } else {
                // No match
                val updatedCards = state.cards.map {
                    if (it.id == flippedIds[0] || it.id == flippedIds[1]) {
                        it.copy(isFaceUp = false)
                    } else {
                        it
                    }
                }
                
                val nextPlayer = if (state.currentPlayer == Player.A) Player.B else Player.A

                updateState {
                    it.copy(
                        cards = updatedCards,
                        currentPlayer = nextPlayer,
                        flippedIds = emptyList(),
                        inputLocked = false
                    )
                }
            }
        }
    }

    private fun isSameContent(c1: CardContent, c2: CardContent): Boolean {
        return when {
            c1 is CardContent.Emoji && c2 is CardContent.Emoji -> c1.value == c2.value
            c1 is CardContent.Number && c2 is CardContent.Number -> c1.value == c2.value
            c1 is CardContent.Color && c2 is CardContent.Color -> c1.value == c2.value
            else -> false
        }
    }
}
