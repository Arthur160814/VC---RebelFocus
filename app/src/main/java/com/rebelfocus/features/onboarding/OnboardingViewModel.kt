package com.rebelfocus.features.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebelfocus.core.common.permissions.PermissionChecker
import com.rebelfocus.core.common.permissions.PermissionState
import com.rebelfocus.core.data.datastore.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class OnboardingStep { Welcome, Accessibility, Notifications, Summary }

data class OnboardingUiState(
    val step: OnboardingStep = OnboardingStep.Welcome,
    val permissions: PermissionState = PermissionState()
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val permissionChecker: PermissionChecker,
    private val preferencesDataStore: UserPreferencesDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingUiState())
    val state: StateFlow<OnboardingUiState> = _state.asStateFlow()

    init { refreshPermissions() }

    fun refreshPermissions() {
        _state.update { it.copy(permissions = permissionChecker.check()) }
    }

    fun nextStep() {
        _state.update {
            val next = when (it.step) {
                OnboardingStep.Welcome -> OnboardingStep.Accessibility
                OnboardingStep.Accessibility -> OnboardingStep.Notifications
                OnboardingStep.Notifications -> OnboardingStep.Summary
                OnboardingStep.Summary -> OnboardingStep.Summary
            }
            it.copy(step = next)
        }
    }

    fun previousStep() {
        _state.update {
            val prev = when (it.step) {
                OnboardingStep.Welcome -> OnboardingStep.Welcome
                OnboardingStep.Accessibility -> OnboardingStep.Welcome
                OnboardingStep.Notifications -> OnboardingStep.Accessibility
                OnboardingStep.Summary -> OnboardingStep.Notifications
            }
            it.copy(step = prev)
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            preferencesDataStore.setOnboardingCompleted(true)
        }
    }
}
