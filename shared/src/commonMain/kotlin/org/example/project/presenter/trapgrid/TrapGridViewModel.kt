package org.example.project.presenter.trapgrid

import org.example.project.base.BaseViewModel
import org.example.project.model.ContentMode
import org.example.project.model.GridSize

class TrapGridViewModel : BaseViewModel<GameState, TrapGridEvent>(GameState()) {

    fun dispatch(action: GameAction) {
        updateState { state ->
            reduce(state, action)
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
