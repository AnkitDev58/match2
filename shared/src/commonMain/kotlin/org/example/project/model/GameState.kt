package org.example.project.model

data class GameState(
    val gridSize: GridSize = GridSize.FOUR_BY_FOUR,
    val contentMode: ContentMode = ContentMode.EMOJI,
    val cards: List<CardState> = emptyList(),
    val currentPlayer: Player = Player.A,
    val scoreA: Int = 0,
    val scoreB: Int = 0,
    val flippedIds: List<Int> = emptyList(),
    val inputLocked: Boolean = false,
    val isGameOver: Boolean = false,
    val showSetup: Boolean = true
)
