package com.justplay.habittracker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justplay.data.db.entity.MoodLogEntity
import com.justplay.data.db.repo.MoodRepo
import com.justplay.habittracker.ui.uiEvent.MoodStatEvent
import com.justplay.habittracker.ui.uiState.MoodStatUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MoodStatViewModel @Inject constructor(
    private val repo: MoodRepo
): ViewModel() {
    private val _uiState = MutableStateFlow(MoodStatUiState())
    val uiState = _uiState.asStateFlow()

    init {
        Timber.tag(TAG).d("init")
        // Adjust to a Month Range
        viewModelScope.launch(Dispatchers.IO) {
            val entity = repo.getLogByDate(LocalDate.now())
            if (entity == null) {
                Timber.tag(TAG).d("entity is null")
            } else {
                Timber.tag(TAG).d("mood = ${entity.moodValue}")
                Timber.tag(TAG).d("feeling = ${entity.feelingValue}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.tag(TAG).d("onCleared")
    }

    fun onEvent(event: MoodStatEvent) {
        when(event) {
            is MoodStatEvent.MoodChanged -> {
                _uiState.update {
                    it.copy(
                        moodValue = event.moodValue,
                        feelingValue = event.feelingValue
                    )
                }
                viewModelScope.launch(Dispatchers.IO) {
                    repo.upsert(
                        MoodLogEntity(
                            date = event.date,
                            feelingValue = event.feelingValue,
                            moodValue = event.moodValue
                        )
                    )
                    Timber.tag(TAG).d("upsert success")
                }
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