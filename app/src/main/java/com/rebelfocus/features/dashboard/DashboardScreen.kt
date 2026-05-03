package com.rebelfocus.features.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToProfiles: () -> Unit,
    onNavigateToAppPicker: () -> Unit,
    onNavigateToAutomations: () -> Unit,
    onNavigateToBackup: () -> Unit,
    onNavigateToLogs: () -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToProfileEdit: (String) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshPermissions()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) { viewModel.refreshPermissions() }

    var showCancelDialog by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    var showEmergencyDialog by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    if (showCancelDialog) {
        com.rebelfocus.core.ui.components.ConfirmationDialog(
            title = "Cancel Session?",
            message = "This will end your current focus session immediately. Your progress will not be marked as completed.",
            confirmText = "End Session",
            isDestructive = true,
            onConfirm = {
                state.activeSession?.let { viewModel.cancelSession(it.id) }
                showCancelDialog = false
            },
            onDismiss = { showCancelDialog = false }
        )
    }

    if (showEmergencyDialog) {
        com.rebelfocus.core.ui.components.ConfirmationDialog(
            title = "Emergency Exit?",
            message = "Use this only if you must bypass all blocking immediately. This action is logged for accountability.",
            confirmText = "Emergency Exit",
            isDestructive = true,
            onConfirm = {
                state.activeSession?.let { viewModel.emergencyExit(it.id) }
                showEmergencyDialog = false
            },
            onDismiss = { showEmergencyDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rebel Focus") },
                actions = {
                    IconButton(onClick = onNavigateToStats) {
                        Icon(Icons.Default.Check, "Stats")
                    }
                    IconButton(onClick = onNavigateToBackup) {
                        Icon(Icons.Default.Settings, "Settings")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Setup Guidance Section
            val missingPermissions = !state.permissions.accessibilityEnabled || 
                                   !state.permissions.exactAlarmsEnabled ||
                                   !state.permissions.batteryOptimizationDisabled

            if (missingPermissions) {
                item {
                    SetupGuidanceCard(
                        permissions = state.permissions,
                        onFixAccessibility = {
                            val intent = android.content.Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS)
                            context.startActivity(intent)
                        },
                        onFixAlarm = {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                                val intent = android.content.Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                                context.startActivity(intent)
                            }
                        },
                        onFixBattery = {
                            val intent = android.content.Intent(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                                data = android.net.Uri.parse("package:${context.packageName}")
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            } else {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Ready to Focus", style = MaterialTheme.typography.titleSmall)
                            Text("System reliability is optimal. Your schedules and blocking will work as expected.", 
                                 style = MaterialTheme.typography.bodySmall)
                            TextButton(onClick = onNavigateToLogs) {
                                Text("View Diagnostic Logs")
                            }
                        }
                    }
                }
            }

            // Permission warnings
            if (!state.permissions.accessibilityEnabled) {
                item {
                    Card(colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer)) {
                        Text("Accessibility Service is not enabled. " +
                                "Blocking will not work without it.",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer)
                    }
                }
            }

@Composable
fun ActiveSessionCard(
    session: com.rebelfocus.core.model.Session,
    onCancel: () -> Unit,
    onEmergencyExit: () -> Unit
) {
    var currentTime by remember { mutableStateOf(android.os.SystemClock.elapsedRealtime()) }
    
    LaunchedEffect(session.id, session.state) {
        while (true) {
            currentTime = android.os.SystemClock.elapsedRealtime()
            kotlinx.coroutines.delay(1000)
        }
    }

    val remaining = if (session.state == com.rebelfocus.core.model.SessionState.Break) {
        val breakElapsed = (currentTime - (session.startTimeElapsedRealtime ?: currentTime)).coerceAtLeast(0)
        (session.breakDurationMillis - breakElapsed).coerceAtLeast(0)
    } else {
        val focusPerInterval = session.plannedDurationMillis
        val focusElapsed = session.elapsedAtPauseMillis + if (session.state == com.rebelfocus.core.model.SessionState.ActiveFocus) {
            (currentTime - (session.startTimeElapsedRealtime ?: currentTime)).coerceAtLeast(0)
        } else 0L
        
        // For Pomodoro, focus time resets each interval in the UI
        val elapsedThisInterval = if (session.pomodoroTarget > 1) {
            focusElapsed - (session.pomodoroCount.toLong() * focusPerInterval)
        } else {
            focusElapsed
        }
        (focusPerInterval - elapsedThisInterval).coerceAtLeast(0)
    }

    val minutes = remaining / 60000
    val seconds = (remaining % 60000) / 1000
    val timerText = "%02d:%02d".format(minutes, seconds)

    Card(
        Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = when (session.state) {
                            com.rebelfocus.core.model.SessionState.ActiveFocus -> "FOCUSING"
                            com.rebelfocus.core.model.SessionState.Break -> "BREAK"
                            com.rebelfocus.core.model.SessionState.Paused -> "PAUSED"
                            else -> "SESSION"
                        },
                        fontWeight = FontWeight.Black,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        timerText,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (session.pomodoroTarget > 1) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Progress", style = MaterialTheme.typography.labelSmall)
                        Text(
                            "${session.pomodoroCount} / ${session.pomodoroTarget}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = onEmergencyExit,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Emergency Exit")
                }
            }
        }
    }
}

            // Active session card
            state.activeSession?.let { session ->
                item {
                    ActiveSessionCard(
                        session = session,
                        onCancel = { showCancelDialog = true },
                        onEmergencyExit = { showEmergencyDialog = true }
                    )
                }
            }

            // Profiles section
            item {
                Row(Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text("Profiles", fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium)
                    TextButton(onClick = onNavigateToProfiles) { Text("Manage") }
                }
            }

            if (state.profiles.isEmpty()) {
                item {
                    Card(Modifier.fillMaxWidth().clickable { onNavigateToProfiles() }) {
                        Text("No profiles yet. Tap to create one.",
                            modifier = Modifier.padding(16.dp))
                    }
                }
            } else {
                items(state.profiles, key = { it.id }) { profile ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { viewModel.startSession(profile.id) },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.PlayArrow,
                                    contentDescription = "Start Focus",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(Modifier.width(16.dp))
                            Column(Modifier.weight(1f)) {
                                Text(profile.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                                Text("${profile.sessionType} · " +
                                        "${profile.focusDurationMillis / 60_000}min",
                                    style = MaterialTheme.typography.bodySmall)
                            }
                            IconButton(onClick = { onNavigateToProfileEdit(profile.id) }) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.Edit,
                                    contentDescription = "Edit Profile",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Quick actions
            item {
                OutlinedButton(
                    onClick = onNavigateToAutomations, 
                    Modifier.fillMaxWidth()
                ) {
                    Text("Manage Automations")
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = onNavigateToAppPicker, Modifier.fillMaxWidth()) {
                    Text("Manage Blocked Apps")
                }
            }

            item {
                Spacer(Modifier.height(32.dp))
                Box(Modifier.fillMaxWidth(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text(
                        text = "release 1.0.21",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SetupGuidanceCard(
    permissions: com.rebelfocus.core.common.permissions.PermissionState,
    onFixAccessibility: () -> Unit,
    onFixAlarm: () -> Unit,
    onFixBattery: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Complete Setup", style = MaterialTheme.typography.titleMedium)
            Text("The following permissions are required for reliable operation:", 
                 style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(12.dp))

            PermissionRow(
                label = "Accessibility Service",
                description = "Required to block apps during focus.",
                isGranted = permissions.accessibilityEnabled,
                onFix = onFixAccessibility
            )
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            PermissionRow(
                label = "Exact Alarms",
                description = "Required to start sessions exactly on time.",
                isGranted = permissions.exactAlarmsEnabled,
                onFix = onFixAlarm
            )
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            PermissionRow(
                label = "Battery Optimization",
                description = "Ensures the app isn't killed in the background.",
                isGranted = permissions.batteryOptimizationDisabled,
                onFix = onFixBattery
            )
        }
    }
}

@Composable
fun PermissionRow(
    label: String,
    description: String,
    isGranted: Boolean,
    onFix: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text(description, style = MaterialTheme.typography.labelSmall)
        }
        if (isGranted) {
            Icon(Icons.Default.Check, contentDescription = "Granted", tint = MaterialTheme.colorScheme.primary)
        } else {
            Button(onClick = onFix, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)) {
                Text("Fix", fontSize = 11.sp)
            }
        }
    }
}

