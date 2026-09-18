package org.example.project.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

abstract class BaseViewModel<State, Event>(initialState: State) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<Event>()
    val uiEvent: SharedFlow<Event> = _uiEvent.asSharedFlow()

    protected val currentState: State
        get() = _uiState.value

    protected fun updateState(update: (State) -> State) {
        _uiState.update(update)
    }

    protected fun sendEvent(event: Event) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}
