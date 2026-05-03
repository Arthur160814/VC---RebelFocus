package com.rebelfocus.features.profiles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebelfocus.core.data.repository.FocusProfileRepository
import com.rebelfocus.core.model.FocusProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileListUiState(
    val profiles: List<FocusProfile> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ProfileListViewModel @Inject constructor(
    private val profileRepository: FocusProfileRepository,
    private val blockingDecisionEngine: com.rebelfocus.core.domain.blocking.BlockingDecisionEngine
) : ViewModel() {

    val state: StateFlow<ProfileListUiState> = profileRepository.observeAll()
        .map { ProfileListUiState(profiles = it, isLoading = false) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProfileListUiState())

    fun deleteProfile(id: String) {
        viewModelScope.launch { 
            profileRepository.deleteById(id) 
            blockingDecisionEngine.invalidateCache()
        }
    }
}
