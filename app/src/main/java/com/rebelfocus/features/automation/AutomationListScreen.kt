package com.rebelfocus.features.automation

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
import com.rebelfocus.core.model.AutomationRule
import com.rebelfocus.core.model.ScheduleType

import com.rebelfocus.core.ui.components.EmptyStateView
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutomationListScreen(
    onNavigateUp: () -> Unit,
    onNavigateToEdit: (ruleId: String?) -> Unit,
    viewModel: AutomationListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Automations") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToEdit(null) }) {
                Icon(Icons.Default.Add, contentDescription = "Add Rule")
            }
        }
    ) { padding ->
        if (state.rules.isEmpty()) {
            EmptyStateView(
                title = "No Automations",
                message = "Automations allow you to schedule focus sessions based on time or calendar events.",
                icon = Icons.Default.DateRange,
                actionText = "Add Automation",
                onAction = { onNavigateToEdit(null) },
                modifier = Modifier.padding(padding)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.rules) { rule ->
                    AutomationRuleItem(
                        rule = rule,
                        onToggle = { viewModel.toggleRule(rule) },
                        onDelete = { viewModel.deleteRule(rule) },
                        onClick = { onNavigateToEdit(rule.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun AutomationRuleItem(
    rule: AutomationRule,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = rule.name, style = MaterialTheme.typography.titleMedium)
                val typeStr = when(rule.scheduleType) {
                    ScheduleType.OneTime -> "One-time"
                    ScheduleType.RecurringWeekday -> "Recurring"
                    ScheduleType.CalendarEvent -> "Calendar"
                }
                Text(text = typeStr, style = MaterialTheme.typography.bodySmall)
            }
            Switch(
                checked = rule.isEnabled,
                onCheckedChange = { onToggle() }
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
