package com.rebelfocus.features.automation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebelfocus.core.data.repository.AutomationRuleRepository
import com.rebelfocus.core.domain.usecase.CancelScheduledSessionUseCase
import com.rebelfocus.core.domain.usecase.ScheduleSessionUseCase
import com.rebelfocus.core.model.AutomationRule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AutomationListUiState(
    val rules: List<AutomationRule> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class AutomationListViewModel @Inject constructor(
    private val ruleRepository: AutomationRuleRepository,
    private val scheduleSessionUseCase: ScheduleSessionUseCase,
    private val cancelScheduledSessionUseCase: CancelScheduledSessionUseCase
) : ViewModel() {

    val state: StateFlow<AutomationListUiState> = ruleRepository.observeAll()
        .map { AutomationListUiState(rules = it, isLoading = false) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AutomationListUiState())

    fun toggleRule(rule: AutomationRule) {
        viewModelScope.launch {
            val updated = rule.copy(isEnabled = !rule.isEnabled)
            ruleRepository.update(updated)
            if (updated.isEnabled) {
                scheduleSessionUseCase(updated)
            } else {
                cancelScheduledSessionUseCase(updated.id)
            }
        }
    }

    fun deleteRule(rule: AutomationRule) {
        viewModelScope.launch {
            cancelScheduledSessionUseCase(rule.id)
            ruleRepository.deleteById(rule.id)
        }
    }
}
