package com.rebelfocus.features.profiles

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import com.rebelfocus.core.ui.components.EmptyStateView
import androidx.compose.material.icons.filled.Face

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileListScreen(
    onNavigateToEdit: (String?) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: ProfileListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Focus Profiles") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToEdit(null) }) {
                Icon(Icons.Default.Add, "Create profile")
            }
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.profiles.isEmpty()) {
            EmptyStateView(
                title = "No Profiles",
                message = "A profile defines which apps to block and for how long. Create one to get started.",
                icon = Icons.Default.Face,
                actionText = "Create Profile",
                onAction = { onNavigateToEdit(null) },
                modifier = Modifier.padding(padding)
            )
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(state.profiles, key = { it.id }) { profile ->
                    ListItem(
                        headlineContent = { Text(profile.name) },
                        supportingContent = {
                            Text("${profile.sessionType} · " +
                                    "${profile.focusDurationMillis / 60_000}min · " +
                                    "${profile.blockedApps.size} apps")
                        },
                        trailingContent = {
                            IconButton(onClick = { viewModel.deleteProfile(profile.id) }) {
                                Icon(Icons.Default.Delete, "Delete")
                            }
                        },
                        modifier = Modifier.clickable { onNavigateToEdit(profile.id) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
