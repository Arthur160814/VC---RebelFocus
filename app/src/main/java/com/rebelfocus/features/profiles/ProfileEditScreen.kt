package com.rebelfocus.features.profiles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rebelfocus.core.model.SessionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAppPicker: () -> Unit,
    viewModel: ProfileEditViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.savedSuccessfully) {
        if (state.savedSuccessfully == true) onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isNew) "New Profile" else "Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { viewModel.setName(it) },
                    label = { Text("Profile Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                Text("Session Type", fontWeight = FontWeight.SemiBold)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SessionType.entries.forEach { type ->
                        FilterChip(
                            selected = state.sessionType == type,
                            onClick = { viewModel.setSessionType(type) },
                            label = { Text(type.name) }
                        )
                    }
                }
            }

            item {
                Text("Focus Duration: ${state.focusDurationMinutes} min", fontWeight = FontWeight.SemiBold)
                Slider(
                    value = state.focusDurationMinutes.toFloat(),
                    onValueChange = { viewModel.setFocusDuration(it.toInt()) },
                    valueRange = 1f..120f, steps = 0
                )
            }

            if (state.sessionType == SessionType.Pomodoro) {
                item {
                    Text("Break Duration: ${state.breakDurationMinutes} min", fontWeight = FontWeight.SemiBold)
                    Slider(
                        value = state.breakDurationMinutes.toFloat(),
                        onValueChange = { viewModel.setBreakDuration(it.toInt()) },
                        valueRange = 1f..30f, steps = 0
                    )
                }
                item {
                    Text("Pomodoro Intervals: ${state.pomodoroTarget}", fontWeight = FontWeight.SemiBold)
                    Slider(
                        value = state.pomodoroTarget.toFloat(),
                        onValueChange = { viewModel.setPomodoroTarget(it.toInt()) },
                        valueRange = 1f..12f, steps = 11
                    )
                }
            }

            item {
                Column {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Extreme Focus Mode", fontWeight = FontWeight.Bold)
                        Switch(checked = state.isExtremeMode, onCheckedChange = { viewModel.setExtremeMode(it) })
                    }
                    Text(
                        "Adds high friction to session termination. Includes a 30s cooldown before you can emergency exit.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (state.isExtremeMode) {
                item {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Ultimate Extreme Mode", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                            Switch(
                                checked = state.isUltimateMode, 
                                onCheckedChange = { viewModel.setUltimateMode(it) },
                                colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.error)
                            )
                        }
                        Text(
                            "Removes the visible Emergency Exit path. You will be locked in until the session ends. Use with caution.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Set as Default", fontWeight = FontWeight.SemiBold)
                    Switch(checked = state.isDefault, onCheckedChange = { viewModel.setDefault(it) })
                }
            }

            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Blocked Apps (${state.attachedPackageNames.size})",
                        fontWeight = FontWeight.SemiBold)
                    TextButton(onClick = onNavigateToAppPicker) { Text("Manage Apps") }
                }
            }

            items(state.allBlockedApps.filter { it.packageName in state.attachedPackageNames }) { app ->
                ListItem(
                    headlineContent = { Text(app.appName) },
                    trailingContent = {
                        Checkbox(
                            checked = true,
                            onCheckedChange = { viewModel.toggleApp(app.packageName) }
                        )
                    }
                )
            }

            // Show unattached blocked apps
            val unattached = state.allBlockedApps.filter { it.packageName !in state.attachedPackageNames }
            if (unattached.isNotEmpty()) {
                item {
                    Text("Available Apps", style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                items(unattached) { app ->
                    ListItem(
                        headlineContent = { Text(app.appName) },
                        trailingContent = {
                            Checkbox(
                                checked = false,
                                onCheckedChange = { viewModel.toggleApp(app.packageName) }
                            )
                        }
                    )
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
            item {
                Button(
                    onClick = { viewModel.save() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isSaving
                ) {
                    if (state.isSaving) CircularProgressIndicator(Modifier.size(20.dp))
                    else Text("Save Profile")
                }
            }
            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}
