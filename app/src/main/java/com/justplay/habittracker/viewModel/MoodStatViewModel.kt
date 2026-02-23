package com.justplay.habittracker.viewModel

import androidx.lifecycle.ViewModel
import com.justplay.habittracker.ui.uiEvent.MoodStatEvent
import com.justplay.habittracker.ui.uiState.MoodStatUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

class MoodStatViewModel @Inject constructor(
    // TODO Add MoodStatRepo
): ViewModel() {
    private val _uiState = MutableStateFlow(MoodStatUiState())
    val uiState = _uiState.asStateFlow()

    init {
        Timber.tag(TAG).d("init")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.tag(TAG).d("onCleared")
    }

    fun onEvent(event: MoodStatEvent) {
        when(event) {
            is MoodStatEvent.FeelingChanged -> {
                _uiState.update { it.copy(feelingValue = event.feelingValue) }
                Timber.tag(TAG).d("feelingValue = ${event.feelingValue}")
            }

            is MoodStatEvent.MoodChanged -> {
                _uiState.update { it.copy(moodValue = event.moodValue) }
                Timber.tag(TAG).d("moodValue = ${event.moodValue}")
            }

            is MoodStatEvent.HideAddMood -> {
                _uiState.update { it.copy(showAddMood = false) }
            }
            is MoodStatEvent.ShowAddMood -> {
                _uiState.update { it.copy(showAddMood = true) }
            }
        }
    }

    companion object {
        private const val TAG = "MoodStatViewModel"
    }
}