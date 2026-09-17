package org.example.project.logic

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.example.project.model.*

class MemoryGameViewModelTest {

    @Test
    fun testInitialState() {
        val viewModel = MemoryGameViewModel()
        val state = viewModel.uiState.value
        
        assertTrue(state.showSetup)
        assertTrue(state.cards.isEmpty())
        assertEquals(Player.A, state.currentPlayer)
        assertEquals(0, state.scoreA)
        assertEquals(0, state.scoreB)
        assertFalse(state.isGameOver)
    }

    @Test
    fun testStartGame4x4() {
        val viewModel = MemoryGameViewModel()
        viewModel.startGame(GridSize.FOUR_BY_FOUR, ContentMode.EMOJI)
        
        val state = viewModel.uiState.value
        assertFalse(state.showSetup)
        assertEquals(16, state.cards.size)
        assertEquals(8, state.cards.groupBy { (it.content as CardContent.Emoji).value }.size)
    }

    @Test
    fun testStartGame5x5FreeTile() {
        val viewModel = MemoryGameViewModel()
        viewModel.startGame(GridSize.FIVE_BY_FIVE, ContentMode.EMOJI)
        
        val state = viewModel.uiState.value
        assertEquals(25, state.cards.size)
        
        val freeTile = state.cards[12]
        assertTrue(freeTile.isFreeTile)
        assertTrue(freeTile.isFaceUp)
        assertTrue(freeTile.isMatched)
        
        // 12 pairs = 24 cards (excluding free tile)
        assertEquals(12, state.cards.filter { !it.isFreeTile }.groupBy { (it.content as CardContent.Emoji).value }.size)
    }

    @Test
    fun testCardClickFlipsCard() {
        val viewModel = MemoryGameViewModel()
        viewModel.startGame(GridSize.FOUR_BY_FOUR, ContentMode.EMOJI)
        viewModel.onCardClicked(0)
        
        val state = viewModel.uiState.value
        assertTrue(state.cards[0].isFaceUp)
        assertEquals(listOf(0), state.flippedIds)
    }

    @Test
    fun testFreeTileClickIgnored() {
        val viewModel = MemoryGameViewModel()
        viewModel.startGame(GridSize.FIVE_BY_FIVE, ContentMode.EMOJI)
        viewModel.onCardClicked(12) // Free tile
        
        val state = viewModel.uiState.value
        assertEquals(emptyList(), state.flippedIds)
    }

    @Test
    fun testNewGameResetsState() {
        val viewModel = MemoryGameViewModel()
        viewModel.startGame(GridSize.FOUR_BY_FOUR, ContentMode.EMOJI)
        viewModel.onCardClicked(0)
        viewModel.newGame()
        
        val state = viewModel.uiState.value
        assertFalse(state.cards[0].isFaceUp)
        assertEquals(emptyList(), state.flippedIds)
        assertEquals(0, state.scoreA)
    }
}
