package org.example.project.presenter.home

import org.example.project.base.BaseViewModel





class HomeViewModel : BaseViewModel<HomeState, HomeEvent>(HomeState()) {
    
    fun onGameSelected(gameId: String) {
        if (gameId == "match_up") {
            sendEvent(HomeEvent.NavigateToMatchUp)
        }
    }
}
