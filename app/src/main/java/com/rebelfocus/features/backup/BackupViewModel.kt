package com.rebelfocus.features.backup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebelfocus.core.domain.usecase.ExportBackupUseCase
import com.rebelfocus.core.domain.usecase.ImportBackupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BackupUiState(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val exportBackupUseCase: ExportBackupUseCase,
    private val importBackupUseCase: ImportBackupUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BackupUiState())
    val state: StateFlow<BackupUiState> = _state.asStateFlow()

    fun exportToUri(uri: Any) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, successMessage = null, errorMessage = null) }
            exportBackupUseCase(uri)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, successMessage = "Backup exported successfully!") }
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, errorMessage = "Export failed: ${e.message}") }
                }
        }
    }

    fun importFromUri(uri: Any) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, successMessage = null, errorMessage = null) }
            importBackupUseCase(uri)
                .onSuccess { count ->
                    _state.update { it.copy(isLoading = false, successMessage = "Restored $count profiles successfully!") }
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, errorMessage = "Import failed: ${e.message}") }
                }
        }
    }

    fun clearMessages() {
        _state.update { it.copy(successMessage = null, errorMessage = null) }
    }
}
