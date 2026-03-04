package com.justplay.habittracker.viewModel.mood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.justplay.data.db.repo.MoodRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MoodHistoryViewModel @Inject constructor(
    private val repo: MoodRepo
): ViewModel() {
    val moodPagingFlow =
        repo.observeMoodPager()
            .cachedIn(viewModelScope)

    init {
        Timber.tag(TAG).d("init")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.tag(TAG).d("onCleared")
    }

    companion object {
        private const val TAG = "MoodHistoryViewModel"
    }
}