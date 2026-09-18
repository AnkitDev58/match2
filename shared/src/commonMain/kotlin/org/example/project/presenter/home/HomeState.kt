package org.example.project.presenter.home

import org.example.project.model.GameItem

data class HomeState(
    val games: List<GameItem> = listOf(
        GameItem("match_up", "Match Up", "A fun emoji/number/color memory matching game!")
    )
)