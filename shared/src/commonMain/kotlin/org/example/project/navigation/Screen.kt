package org.example.project.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data object MatchUp : Screen

    @Serializable
    data object TrapGrid : Screen
}
