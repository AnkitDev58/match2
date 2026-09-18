package org.example.project.presenter.trapgrid

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.base.BaseViewModel
import org.example.project.model.ContentMode
import org.example.project.model.GridSize

class TrapGridViewModel : BaseViewModel<GameState, TrapGridEvent>(GameState()) {

    fun dispatch(action: GameAction) {
        updateState { state ->
            reduce(state, action)
        }

        val newState = currentState
        if (action is GameAction.PickSecret) {
            if (newState.phase == GamePhase.RevealingA || newState.phase == GamePhase.RevealingB) {
                viewModelScope.launch {
                    delay(2000)
                    dispatch(GameAction.ConfirmSecretReveal)
                }
            }
        }
    }

    fun startGame(gridSize: GridSize, contentMode: ContentMode) {
        dispatch(GameAction.SelectSize(gridSize, contentMode))
    }

    fun goToSetup() {
        dispatch(GameAction.NewGame)
    }

    fun onNavigateBack() {
        sendEvent(TrapGridEvent.NavigateBack)
    }
}
