package com.rebelfocus.features.automation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.rebelfocus.core.data.repository.AutomationRuleRepository
import com.rebelfocus.core.data.repository.FocusProfileRepository
import com.rebelfocus.core.domain.calendar.CalendarSource
import com.rebelfocus.core.domain.calendar.DeviceCalendar
import com.rebelfocus.core.domain.scheduling.AlarmScheduler
import com.rebelfocus.core.domain.usecase.ScheduleSessionUseCase
import com.rebelfocus.core.model.AutomationRule
import com.rebelfocus.core.model.CalendarMatchConfig
import com.rebelfocus.core.model.FocusProfile
import com.rebelfocus.core.model.ScheduleType
import com.rebelfocus.services.calendar.CalendarSyncWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class AutomationEditUiState(
    val id: String = "",
    val name: String = "",
    val profileId: String = "",
    val scheduleType: ScheduleType = ScheduleType.OneTime,
    val startTimeMinutes: Int = 8 * 60, // 8:00 AM default
    val daysOfWeek: Set<Int> = emptySet(),
    val availableProfiles: List<FocusProfile> = emptyList(),
    val hasExactAlarmPermission: Boolean = true,
    // Calendar fields
    val hasCalendarPermission: Boolean = false,
    val availableCalendars: List<DeviceCalendar> = emptyList(),
    val selectedCalendarId: String = "",
    val titleKeyword: String = "",
    val requireBusyStatus: Boolean = false,
    val timeWindowStartMinutes: Int? = null,
    val timeWindowEndMinutes: Int? = null,
    // State
    val isSaving: Boolean = false,
    val savedSuccessfully: Boolean = false,
    val validationError: String? = null
)

@HiltViewModel
class AutomationEditViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    savedStateHandle: SavedStateHandle,
    private val ruleRepository: AutomationRuleRepository,
    private val profileRepository: FocusProfileRepository,
    private val scheduleSessionUseCase: ScheduleSessionUseCase,
    private val alarmScheduler: AlarmScheduler,
    private val calendarSource: CalendarSource
) : ViewModel() {

    private val ruleId: String? = savedStateHandle["ruleId"]
    private val _state = MutableStateFlow(AutomationEditUiState())
    val state: StateFlow<AutomationEditUiState> = _state.asStateFlow()

    init {
        checkPermissions()
        loadData()
    }

    fun checkPermissions() {
        val hasCalendar = ContextCompat.checkSelfPermission(
            appContext, Manifest.permission.READ_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED

        _state.update {
            it.copy(
                hasExactAlarmPermission = alarmScheduler.canScheduleExactAlarms(),
                hasCalendarPermission = hasCalendar
            )
        }

        if (hasCalendar) {
            loadCalendars()
        }
    }

    private fun loadCalendars() {
        viewModelScope.launch {
            val calendars = calendarSource.getAvailableCalendars()
            _state.update { it.copy(availableCalendars = calendars) }
        }
    }

    fun onCalendarPermissionGranted() {
        _state.update { it.copy(hasCalendarPermission = true) }
        loadCalendars()
        // Trigger an on-demand sync now that permission is available
        triggerOnDemandSync()
    }

    private fun loadData() {
        viewModelScope.launch {
            val profiles = profileRepository.observeAll().first()
            _state.update { it.copy(availableProfiles = profiles) }

            if (ruleId != null) {
                val rule = ruleRepository.getById(ruleId)
                if (rule != null) {
                    _state.update {
                        it.copy(
                            id = rule.id,
                            name = rule.name,
                            profileId = rule.profileId,
                            scheduleType = rule.scheduleType,
                            startTimeMinutes = rule.startTimeMinutes ?: (8 * 60),
                            daysOfWeek = rule.daysOfWeek.toSet(),
                            selectedCalendarId = rule.calendarMatchConfig?.calendarId ?: "",
                            titleKeyword = rule.calendarMatchConfig?.titleKeyword ?: "",
                            requireBusyStatus = rule.calendarMatchConfig?.requireBusyStatus ?: false,
                            timeWindowStartMinutes = rule.calendarMatchConfig?.timeWindowStartMinutes,
                            timeWindowEndMinutes = rule.calendarMatchConfig?.timeWindowEndMinutes
                        )
                    }
                }
            } else {
                _state.update {
                    it.copy(
                        id = UUID.randomUUID().toString(),
                        profileId = profiles.firstOrNull()?.id ?: ""
                    )
                }
            }
        }
    }

    fun setName(name: String) { _state.update { it.copy(name = name) } }
    fun setProfileId(id: String) { _state.update { it.copy(profileId = id) } }
    fun setScheduleType(type: ScheduleType) { _state.update { it.copy(scheduleType = type) } }
    fun setStartTime(minutes: Int) { _state.update { it.copy(startTimeMinutes = minutes) } }
    fun setSelectedCalendarId(id: String) { _state.update { it.copy(selectedCalendarId = id) } }
    fun setTitleKeyword(keyword: String) { _state.update { it.copy(titleKeyword = keyword) } }
    fun setRequireBusyStatus(require: Boolean) { _state.update { it.copy(requireBusyStatus = require) } }
    fun setTimeWindowStart(minutes: Int?) { _state.update { it.copy(timeWindowStartMinutes = minutes) } }
    fun setTimeWindowEnd(minutes: Int?) { _state.update { it.copy(timeWindowEndMinutes = minutes) } }

    fun toggleDay(day: Int) {
        _state.update {
            val updated = if (day in it.daysOfWeek) it.daysOfWeek - day else it.daysOfWeek + day
            it.copy(daysOfWeek = updated)
        }
    }

    fun save() {
        val s = _state.value
        
        // Basic Validation
        val error = when {
            s.name.isBlank() -> "Please enter a name for this automation."
            s.profileId.isBlank() -> "Please select a focus profile."
            s.scheduleType == ScheduleType.RecurringWeekday && s.daysOfWeek.isEmpty() -> 
                "Please select at least one day for this recurring schedule."
            s.scheduleType == ScheduleType.CalendarEvent && s.selectedCalendarId.isBlank() -> 
                "Please select a calendar source."
            else -> null
        }

        if (error != null) {
            _state.update { it.copy(validationError = error) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, validationError = null) }
            val now = System.currentTimeMillis()

            val calendarConfig = if (s.scheduleType == ScheduleType.CalendarEvent && s.selectedCalendarId.isNotBlank()) {
                CalendarMatchConfig(
                    calendarId = s.selectedCalendarId,
                    titleKeyword = s.titleKeyword.ifBlank { null },
                    requireBusyStatus = s.requireBusyStatus,
                    timeWindowStartMinutes = s.timeWindowStartMinutes,
                    timeWindowEndMinutes = s.timeWindowEndMinutes
                )
            } else {
                null
            }

            val rule = AutomationRule(
                id = s.id,
                profileId = s.profileId,
                name = s.name.ifBlank { "Untitled Rule" },
                isEnabled = true,
                scheduleType = s.scheduleType,
                startTimeMinutes = if (s.scheduleType != ScheduleType.CalendarEvent) s.startTimeMinutes else null,
                endTimeMinutes = null,
                daysOfWeek = if (s.scheduleType == ScheduleType.RecurringWeekday) s.daysOfWeek.toList() else emptyList(),
                calendarMatchConfig = calendarConfig,
                createdAt = now,
                updatedAt = now
            )

            if (ruleId != null) {
                ruleRepository.update(rule)
            } else {
                ruleRepository.insert(rule)
            }

            // For manual schedules, schedule via AlarmManager directly
            if (s.scheduleType != ScheduleType.CalendarEvent) {
                scheduleSessionUseCase(rule)
            } else {
                // For calendar rules, trigger an on-demand sync to materialize triggers
                triggerOnDemandSync()
            }

            _state.update { it.copy(isSaving = false, savedSuccessfully = true) }
        }
    }

    private fun triggerOnDemandSync() {
        val request = OneTimeWorkRequestBuilder<CalendarSyncWorker>().build()
        WorkManager.getInstance(appContext)
            .enqueueUniqueWork(
                CalendarSyncWorker.WORK_NAME_ONDEMAND,
                ExistingWorkPolicy.REPLACE,
                request
            )
    }
}
