package com.rebelfocus.features.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebelfocus.core.domain.usecase.GetFocusStatsUseCase
import com.rebelfocus.core.model.FocusStats
import com.rebelfocus.core.model.StatsRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val getFocusStatsUseCase: GetFocusStatsUseCase
) : ViewModel() {

    private val _selectedRange = MutableStateFlow(StatsRange.SEVEN_DAYS)
    val selectedRange: StateFlow<StatsRange> = _selectedRange

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val stats: StateFlow<FocusStats?> = _selectedRange
        .flatMapLatest { range -> getFocusStatsUseCase(range) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun setRange(range: StatsRange) {
        _selectedRange.value = range
    }
}
