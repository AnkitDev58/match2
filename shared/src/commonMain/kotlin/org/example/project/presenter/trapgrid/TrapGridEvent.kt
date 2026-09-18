package org.example.project.presenter.trapgrid

sealed interface TrapGridEvent {
    data object NavigateBack : TrapGridEvent
}
