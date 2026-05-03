package com.rebelfocus.features.apps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebelfocus.core.common.apps.InstalledAppInfo
import com.rebelfocus.core.common.apps.LauncherAppDiscovery
import com.rebelfocus.core.data.repository.BlockedAppRepository
import com.rebelfocus.core.model.BlockedApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SelectableApp(
    val info: InstalledAppInfo,
    val isSelected: Boolean
)

data class AppPickerUiState(
    val isLoading: Boolean = true,
    val apps: List<SelectableApp> = emptyList(),
    val searchQuery: String = "",
    val selectedCount: Int = 0
) {
    val filteredApps: List<SelectableApp>
        get() = if (searchQuery.isBlank()) apps
        else apps.filter { it.info.appName.contains(searchQuery, ignoreCase = true) }
}

@HiltViewModel
class AppPickerViewModel @Inject constructor(
    private val appDiscovery: LauncherAppDiscovery,
    private val blockedAppRepository: BlockedAppRepository,
    private val blockingDecisionEngine: com.rebelfocus.core.domain.blocking.BlockingDecisionEngine
) : ViewModel() {

    private val _state = MutableStateFlow(AppPickerUiState())
    val state: StateFlow<AppPickerUiState> = _state.asStateFlow()

    init { loadApps() }

    private fun loadApps() {
        viewModelScope.launch {
            val launcher = appDiscovery.queryLauncherApps()
            val blocked = blockedAppRepository.getEnabledPackageNames().toSet()
            val selectable = launcher.map { app ->
                SelectableApp(info = app, isSelected = app.packageName in blocked)
            }
            _state.update {
                it.copy(isLoading = false, apps = selectable,
                    selectedCount = selectable.count { s -> s.isSelected })
            }
        }
    }

    fun toggleApp(packageName: String) {
        _state.update { current ->
            val updated = current.apps.map { app ->
                if (app.info.packageName == packageName) app.copy(isSelected = !app.isSelected)
                else app
            }
            current.copy(apps = updated, selectedCount = updated.count { it.isSelected })
        }
    }

    fun setSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun saveSelections() {
        viewModelScope.launch {
            val currentApps = _state.value.apps
            for (app in currentApps) {
                if (app.isSelected) {
                    blockedAppRepository.insert(
                        BlockedApp(app.info.packageName, app.info.appName, isEnabled = true)
                    )
                } else {
                    blockedAppRepository.deleteByPackageName(app.info.packageName)
                }
            }
            blockingDecisionEngine.invalidateCache()
        }
    }
}
