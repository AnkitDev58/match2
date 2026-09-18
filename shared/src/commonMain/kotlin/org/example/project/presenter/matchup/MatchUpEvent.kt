package org.example.project.presenter.matchup

sealed interface MatchUpEvent {
    data object NavigateBack : MatchUpEvent
}
