package org.example.project.model

sealed class CardContent {
    data class Emoji(val value: String) : CardContent()
    data class Number(val value: Int) : CardContent()
    data class Color(val value: androidx.compose.ui.graphics.Color) : CardContent()
}
