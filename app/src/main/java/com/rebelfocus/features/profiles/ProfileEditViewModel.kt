package com.rebelfocus.features.profiles

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebelfocus.core.data.repository.BlockedAppRepository
import com.rebelfocus.core.data.repository.FocusProfileRepository
import com.rebelfocus.core.model.BlockedApp
import com.rebelfocus.core.model.FocusProfile
import com.rebelfocus.core.model.SessionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class ProfileEditUiState(
    val id: String = "",
    val name: String = "",
    val sessionType: SessionType = SessionType.Pomodoro,
    val focusDurationMinutes: Int = 25,
    val breakDurationMinutes: Int = 5,
    val pomodoroTarget: Int = 4,
    val isDefault: Boolean = false,
    val allBlockedApps: List<BlockedApp> = emptyList(),
    val attachedPackageNames: Set<String> = emptySet(),
    val isSaving: Boolean = false,
    val savedSuccessfully: Boolean? = null,
    val isNew: Boolean = true,
    val isExtremeMode: Boolean = false,
    val isUltimateMode: Boolean = false
)

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val profileRepository: FocusProfileRepository,
    private val blockedAppRepository: BlockedAppRepository,
    private val blockingDecisionEngine: com.rebelfocus.core.domain.blocking.BlockingDecisionEngine
) : ViewModel() {

    private val profileId: String? = savedStateHandle["profileId"]
    private val _state = MutableStateFlow(ProfileEditUiState())
    val state: StateFlow<ProfileEditUiState> = _state.asStateFlow()

    init { loadData() }

    private fun loadData() {
        viewModelScope.launch {
            // Load all blocked apps for selection
            blockedAppRepository.observeAll().collect { allApps ->
                _state.update { it.copy(allBlockedApps = allApps) }
            }
        }
        viewModelScope.launch {
            if (profileId != null) {
                val profile = profileRepository.getById(profileId)
                if (profile != null) {
                    _state.update {
                        it.copy(
                            id = profile.id,
                            name = profile.name,
                            sessionType = profile.sessionType,
                            focusDurationMinutes = (profile.focusDurationMillis / 60_000).toInt(),
                            breakDurationMinutes = (profile.breakDurationMillis / 60_000).toInt(),
                            pomodoroTarget = profile.pomodoroTarget,
                            isDefault = profile.isDefault,
                            isExtremeMode = profile.isExtremeMode,
                            isUltimateMode = profile.isUltimateMode,
                            attachedPackageNames = profile.blockedApps.map { a -> a.packageName }.toSet(),
                            isNew = false
                        )
                    }
                }
            } else {
                _state.update { it.copy(id = UUID.randomUUID().toString()) }
            }
        }
    }

    fun setName(name: String) { _state.update { it.copy(name = name) } }
    fun setSessionType(type: SessionType) { _state.update { it.copy(sessionType = type) } }
    fun setFocusDuration(min: Int) { _state.update { it.copy(focusDurationMinutes = min.coerceIn(1, 120)) } }
    fun setBreakDuration(min: Int) { _state.update { it.copy(breakDurationMinutes = min.coerceIn(1, 30)) } }
    fun setPomodoroTarget(count: Int) { _state.update { it.copy(pomodoroTarget = count.coerceIn(1, 12)) } }
    fun setDefault(v: Boolean) { _state.update { it.copy(isDefault = v) } }
    fun setExtremeMode(v: Boolean) { 
        _state.update { it.copy(isExtremeMode = v, isUltimateMode = if (!v) false else it.isUltimateMode) } 
    }
    fun setUltimateMode(v: Boolean) { _state.update { it.copy(isUltimateMode = v) } }

    fun toggleApp(packageName: String) {
        _state.update {
            val updated = if (packageName in it.attachedPackageNames)
                it.attachedPackageNames - packageName
            else it.attachedPackageNames + packageName
            it.copy(attachedPackageNames = updated)
        }
    }

    fun save() {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            try {
                val s = _state.value
                val now = System.currentTimeMillis()
                val profile = FocusProfile(
                    id = s.id,
                    name = s.name.ifBlank { "Untitled Profile" },
                    isDefault = s.isDefault,
                    sessionType = s.sessionType,
                    isExtremeMode = s.isExtremeMode,
                    isUltimateMode = s.isUltimateMode,
                    focusDurationMillis = s.focusDurationMinutes * 60_000L,
                    breakDurationMillis = s.breakDurationMinutes * 60_000L,
                    pomodoroTarget = s.pomodoroTarget,
                    blockedApps = s.allBlockedApps.filter { it.packageName in s.attachedPackageNames },
                    createdAt = now,
                    updatedAt = now
                )
                if (s.isNew) profileRepository.insert(profile)
                else profileRepository.update(profile)
                blockingDecisionEngine.invalidateCache()
                _state.update { it.copy(isSaving = false, savedSuccessfully = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, savedSuccessfully = false) }
            }
        }
    }
}
