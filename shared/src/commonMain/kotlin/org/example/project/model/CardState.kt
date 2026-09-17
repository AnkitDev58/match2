package org.example.project.model

data class CardState(
    val id: Int,
    val content: CardContent,
    val isFaceUp: Boolean = false,
    val isMatched: Boolean = false,
    val isFreeTile: Boolean = false,
    val matchedBy: Player? = null
)
