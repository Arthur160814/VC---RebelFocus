package com.rebelfocus.features.logging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebelfocus.core.database.dao.DiagnosticLogDao
import com.rebelfocus.core.database.entity.DiagnosticLogEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiagnosticLogViewModel @Inject constructor(
    private val diagnosticLogDao: DiagnosticLogDao
) : ViewModel() {

    val logs: StateFlow<List<DiagnosticLogEntity>> = diagnosticLogDao.observeLatestLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun clearLogs() {
        viewModelScope.launch {
            diagnosticLogDao.clearAll()
        }
    }
}
