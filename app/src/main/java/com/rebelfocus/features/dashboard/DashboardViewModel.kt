package com.rebelfocus.features.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebelfocus.core.common.permissions.PermissionChecker
import com.rebelfocus.core.common.permissions.PermissionState
import com.rebelfocus.core.data.repository.FocusProfileRepository
import com.rebelfocus.core.data.repository.SessionRepository
import com.rebelfocus.core.model.FocusProfile
import com.rebelfocus.core.model.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val profiles: List<FocusProfile> = emptyList(),
    val activeSession: Session? = null,
    val permissions: PermissionState = PermissionState(),
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val profileRepository: FocusProfileRepository,
    private val sessionRepository: SessionRepository,
    private val permissionChecker: PermissionChecker,
    private val stopFocusSessionUseCase: com.rebelfocus.core.domain.usecase.StopFocusSessionUseCase,
    private val startFocusSessionUseCase: com.rebelfocus.core.domain.usecase.StartFocusSessionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardUiState())
    val state: StateFlow<DashboardUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            profileRepository.observeAll().collect { profiles ->
                _state.update { it.copy(profiles = profiles, isLoading = false) }
            }
        }
        viewModelScope.launch {
            sessionRepository.observeActiveSession().collect { session ->
                _state.update { it.copy(activeSession = session) }
            }
        }
    }

    fun refreshPermissions() {
        _state.update { it.copy(permissions = permissionChecker.check()) }
    }

    fun startSession(profileId: String) {
        viewModelScope.launch {
            val profile = _state.value.profiles.find { it.id == profileId } ?: return@launch
            
            // Temporary debugging log
            
            startFocusSessionUseCase(
                profileId = profile.id,
                type = if (profile.sessionType == com.rebelfocus.core.model.SessionType.Pomodoro) com.rebelfocus.core.model.SessionType.Pomodoro else com.rebelfocus.core.model.SessionType.FreeForm,
                focusDurationMillis = profile.focusDurationMillis,
                breakDurationMillis = profile.breakDurationMillis,
                pomodoroTarget = profile.pomodoroTarget,
                isExtremeMode = profile.isExtremeMode,
                isUltimateMode = profile.isUltimateMode
            )
        }
    }

    fun cancelSession(sessionId: String) {
        viewModelScope.launch {
            stopFocusSessionUseCase(sessionId, com.rebelfocus.core.domain.usecase.StopReason.Cancel)
        }
    }

    fun emergencyExit(sessionId: String) {
        viewModelScope.launch {
            stopFocusSessionUseCase(sessionId, com.rebelfocus.core.domain.usecase.StopReason.EmergencyExit)
        }
    }
}
