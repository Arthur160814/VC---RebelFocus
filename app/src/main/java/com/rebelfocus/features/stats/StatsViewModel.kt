package com.rebelfocus.features.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebelfocus.core.domain.usecase.GetFocusStatsUseCase
import com.rebelfocus.core.model.FocusStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    getFocusStatsUseCase: GetFocusStatsUseCase
) : ViewModel() {

    val stats: StateFlow<FocusStats?> = getFocusStatsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}
