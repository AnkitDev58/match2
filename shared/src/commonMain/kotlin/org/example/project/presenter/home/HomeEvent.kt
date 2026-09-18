package org.example.project.presenter.home

sealed interface HomeEvent {
    data object NavigateToMatchUp : HomeEvent
}
