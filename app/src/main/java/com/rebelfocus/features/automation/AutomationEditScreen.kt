package com.rebelfocus.features.automation

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rebelfocus.core.model.ScheduleType
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutomationEditScreen(
    onNavigateUp: () -> Unit,
    viewModel: AutomationEditViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val calendarPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.onCalendarPermissionGranted()
        }
    }

    LaunchedEffect(state.savedSuccessfully) {
        if (state.savedSuccessfully) onNavigateUp()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.id.isEmpty()) "New Rule" else "Edit Rule") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.save() }) {
                Text("Save")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Validation Error
            state.validationError?.let { error ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(
                        text = error,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            // Permission Warning Banner: Exact Alarms
            if (!state.hasExactAlarmPermission) {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Exact Alarms Disabled", style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Android has restricted background scheduling. Without this permission, your schedule will not start precisely on time. It will degrade to an inexact alarm.",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            Button(onClick = {
                                context.startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                            }) {
                                Text("Grant Permission")
                            }
                        }
                    }
                }
            }

            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::setName,
                label = { Text("Rule Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Profile Selection
            Text("Profile: ${state.availableProfiles.find { it.id == state.profileId }?.name ?: "None"}", style = MaterialTheme.typography.bodyMedium)

            Text("Schedule Type", style = MaterialTheme.typography.titleMedium)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = state.scheduleType == ScheduleType.OneTime,
                    onClick = { viewModel.setScheduleType(ScheduleType.OneTime) },
                    label = { Text("One-time") }
                )
                FilterChip(
                    selected = state.scheduleType == ScheduleType.RecurringWeekday,
                    onClick = { viewModel.setScheduleType(ScheduleType.RecurringWeekday) },
                    label = { Text("Recurring") }
                )
                FilterChip(
                    selected = state.scheduleType == ScheduleType.CalendarEvent,
                    onClick = { viewModel.setScheduleType(ScheduleType.CalendarEvent) },
                    label = { Text("Calendar") }
                )
            }

            // Manual schedule fields
            if (state.scheduleType != ScheduleType.CalendarEvent) {
                Text("Start Time (Minutes from Midnight)", style = MaterialTheme.typography.titleMedium)
                Slider(
                    value = state.startTimeMinutes.toFloat(),
                    onValueChange = { viewModel.setStartTime(it.toInt()) },
                    valueRange = 0f..1439f,
                    steps = 1439
                )
                val hours = state.startTimeMinutes / 60
                val mins = state.startTimeMinutes % 60
                Text(String.format(Locale.getDefault(), "%02d:%02d", hours, mins))

                if (state.scheduleType == ScheduleType.RecurringWeekday) {
                    Text("Repeat on", style = MaterialTheme.typography.titleMedium)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        (1..7).forEach { day ->
                            val dayName = DayOfWeek.of(day).getDisplayName(TextStyle.NARROW, Locale.getDefault())
                            FilterChip(
                                selected = day in state.daysOfWeek,
                                onClick = { viewModel.toggleDay(day) },
                                label = { Text(dayName) }
                            )
                        }
                    }
                }
            }

            // Calendar schedule fields
            if (state.scheduleType == ScheduleType.CalendarEvent) {
                HorizontalDivider()
                Text("Calendar Integration", style = MaterialTheme.typography.titleMedium)

                if (!state.hasCalendarPermission) {
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Calendar Permission Required", style = MaterialTheme.typography.titleSmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Rebel Focus needs access to your calendar to read events and match them to focus profiles. This data stays on your device.",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                calendarPermissionLauncher.launch(Manifest.permission.READ_CALENDAR)
                            }) {
                                Text("Grant Calendar Access")
                            }
                        }
                    }
                } else {
                    // Calendar selection
                    if (state.availableCalendars.isNotEmpty()) {
                        Text("Select Calendar", style = MaterialTheme.typography.labelLarge)
                        Column {
                            state.availableCalendars.forEach { calendar ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = state.selectedCalendarId == calendar.id,
                                        onClick = { viewModel.setSelectedCalendarId(calendar.id) }
                                    )
                                    Column(modifier = Modifier.padding(start = 8.dp)) {
                                        Text(calendar.displayName, style = MaterialTheme.typography.bodyMedium)
                                        Text(calendar.accountName, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            "No calendars found on this device.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    // Title keyword filter
                    OutlinedTextField(
                        value = state.titleKeyword,
                        onValueChange = viewModel::setTitleKeyword,
                        label = { Text("Title Keyword (optional)") },
                        placeholder = { Text("e.g. Deep Work, Meeting") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Busy status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = state.requireBusyStatus,
                            onCheckedChange = viewModel::setRequireBusyStatus
                        )
                        Text("Only when marked as Busy", modifier = Modifier.padding(start = 8.dp))
                    }

                    // Time window
                    Text("Time Window (optional)", style = MaterialTheme.typography.labelLarge)
                    Text(
                        "Only match events that start within this time window.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = state.timeWindowStartMinutes?.let { formatMinutes(it) } ?: "",
                            onValueChange = { viewModel.setTimeWindowStart(parseMinutes(it)) },
                            label = { Text("From") },
                            placeholder = { Text("09:00") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = state.timeWindowEndMinutes?.let { formatMinutes(it) } ?: "",
                            onValueChange = { viewModel.setTimeWindowEnd(parseMinutes(it)) },
                            label = { Text("To") },
                            placeholder = { Text("17:00") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

private fun formatMinutes(totalMinutes: Int): String {
    val h = totalMinutes / 60
    val m = totalMinutes % 60
    return String.format(Locale.getDefault(), "%02d:%02d", h, m)
}

private fun parseMinutes(input: String): Int? {
    val parts = input.split(":")
    if (parts.size != 2) return null
    val h = parts[0].toIntOrNull() ?: return null
    val m = parts[1].toIntOrNull() ?: return null
    if (h !in 0..23 || m !in 0..59) return null
    return h * 60 + m
}
