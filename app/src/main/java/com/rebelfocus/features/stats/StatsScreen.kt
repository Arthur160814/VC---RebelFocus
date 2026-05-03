package com.rebelfocus.features.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import com.rebelfocus.core.model.FocusMode
import com.rebelfocus.core.model.FocusStats
import com.rebelfocus.core.model.ModeUsageStats
import com.rebelfocus.core.model.RecentSession
import com.rebelfocus.core.model.StatsRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rebelfocus.core.ui.components.EmptyStateView
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    onNavigateUp: () -> Unit,
    viewModel: StatsViewModel = hiltViewModel()
) {
    val stats by viewModel.stats.collectAsState()
    val selectedRange by viewModel.selectedRange.collectAsState()
    var showGoalDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistics") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        val s = stats
        if (s == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (s.totalSessions == 0) {
            EmptyStateView(
                title = "No Focus Data",
                message = "Your focus history will appear here once you complete your first session. Go to the Dashboard to choose a profile and start focusing.",
                icon = Icons.Default.Info,
                actionText = "Go to Dashboard",
                onAction = onNavigateUp,
                modifier = Modifier.padding(padding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    StatsRangeSelector(
                        selectedRange = selectedRange,
                        onRangeSelected = { viewModel.setRange(it) }
                    )
                }

                stats?.let { s ->
                    item {
                        WeeklyGoalCard(
                            targetMinutes = s.weeklyGoalTargetMinutes,
                            progressMinutes = s.weeklyGoalProgressMinutes,
                            onEditClick = { showGoalDialog = true }
                        )
                    }
                }

                item {
                    Text("Overview", style = MaterialTheme.typography.titleMedium)
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            label = "Total Time",
                            value = formatTotalTime(s.totalFocusTimeMillis),
                            icon = Icons.Default.Check,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "Daily Average",
                            value = formatAverageTime(s.dailyAverageMillis),
                            icon = Icons.Default.List,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            label = "Sessions",
                            value = s.totalSessions.toString(),
                            icon = Icons.Default.CheckCircle,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "Completion",
                            value = "${(s.completionRate * 100).toInt()}%",
                            icon = Icons.Default.Star,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Spacer(Modifier.height(8.dp))
                    Text("Insights", style = MaterialTheme.typography.titleMedium)
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            label = "Current Streak",
                            value = formatStreak(s.currentStreak),
                            icon = Icons.Default.DateRange,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "Longest Streak",
                            value = formatStreak(s.longestStreak),
                            icon = Icons.Default.Star,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            label = "Interrupted",
                            value = s.interruptedCount.toString(),
                            icon = Icons.Default.Warning,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "Best Day",
                            value = s.bestDayName ?: "No data",
                            icon = Icons.Default.Star,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Spacer(Modifier.height(8.dp))
                    Text("Mode Usage", style = MaterialTheme.typography.titleMedium)
                }

                items(listOf(FocusMode.NORMAL, FocusMode.EXTREME, FocusMode.ULTIMATE)) { mode ->
                    val modeStats = s.modeUsage[mode] ?: ModeUsageStats(0, 0, null, 0)
                    val isMostEffective = s.mostEffectiveMode == mode
                    val maxFocusTime = s.modeUsage.values.maxOfOrNull { it.focusTimeMillis }?.coerceAtLeast(1L) ?: 1L
                    
                    ModeUsageRow(
                        mode = mode,
                        stats = modeStats,
                        isMostEffective = isMostEffective,
                        maxFocusTime = maxFocusTime
                    )
                }

                item {
                    Spacer(Modifier.height(8.dp))
                    Text("Recent Sessions", style = MaterialTheme.typography.titleMedium)
                }

                stats?.let { s ->
                    if (s.recentSessions.isEmpty()) {
                        item {
                            Text(
                                "No completed or interrupted sessions yet.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    } else {
                        item {
                            RecentSessionsSection(s.recentSessions)
                        }
                    }
                }

                item {
                    val chartTitle = when (s.selectedRange) {
                        StatsRange.SEVEN_DAYS -> "Focus Minutes - Last 7 Days"
                        StatsRange.THIRTY_DAYS -> "Focus Minutes - Last 30 Days"
                        StatsRange.ALL -> "Recent Focus Minutes"
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(chartTitle, style = MaterialTheme.typography.titleMedium)
                }

                item {
                    WeeklyDistributionChart(s)
                }
            }
        }
    }
    
    if (showGoalDialog) {
        val currentGoal = stats?.weeklyGoalTargetMinutes ?: 120
        WeeklyGoalEditDialog(
            currentGoal = currentGoal,
            onDismiss = { showGoalDialog = false },
            onGoalSelected = {
                viewModel.updateWeeklyGoal(it)
                showGoalDialog = false
            }
        )
    }
}

@Composable
fun WeeklyGoalCard(
    targetMinutes: Int,
    progressMinutes: Int,
    onEditClick: () -> Unit
) {
    val progress = if (targetMinutes > 0) progressMinutes.toFloat() / targetMinutes.toFloat() else 0f
    val isGoalReached = progressMinutes >= targetMinutes

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Weekly Goal",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(onClick = onEditClick, contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)) {
                    Text("Edit", style = MaterialTheme.typography.labelMedium)
                }
            }

            Spacer(Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "$progressMinutes / $targetMinutes min",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isGoalReached) "Goal reached" else "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isGoalReached) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                )
            }

            Spacer(Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress.coerceAtMost(1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surface,
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyGoalEditDialog(
    currentGoal: Int,
    onDismiss: () -> Unit,
    onGoalSelected: (Int) -> Unit
) {
    val presets = listOf(60, 120, 180, 300)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Weekly Goal") },
        text = {
            Column {
                Text(
                    "Choose your weekly focus target:",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    presets.forEach { minutes ->
                        FilterChip(
                            selected = minutes == currentGoal,
                            onClick = { onGoalSelected(minutes) },
                            label = { Text("${minutes}m") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun RecentSessionsSection(sessions: List<RecentSession>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            sessions.forEachIndexed { index, session ->
                RecentSessionRow(session)
                if (index < sessions.size - 1) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}

@Composable
fun RecentSessionRow(session: RecentSession) {
    val durationMinutes = (session.durationMillis / (1000 * 60)).toInt()
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${session.dateLabel} · ${session.mode.name.lowercase().replaceFirstChar { it.uppercase() }}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "${durationMinutes} min · ${session.stateLabel}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
        
        Icon(
            imageVector = if (session.stateLabel == "Completed") Icons.Default.CheckCircle else Icons.Default.Info,
            contentDescription = null,
            tint = if (session.stateLabel == "Completed") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsRangeSelector(
    selectedRange: StatsRange,
    onRangeSelected: (StatsRange) -> Unit
) {
    val ranges = listOf(StatsRange.SEVEN_DAYS, StatsRange.THIRTY_DAYS, StatsRange.ALL)
    val rangeLabels = listOf("7D", "30D", "All")

    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        ranges.forEachIndexed { index, range ->
            SegmentedButton(
                selected = range == selectedRange,
                onClick = { onRangeSelected(range) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = ranges.size),
                label = { Text(rangeLabels[index]) }
            )
        }
    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(8.dp))
            Text(value, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
fun ModeUsageRow(
    mode: FocusMode,
    stats: ModeUsageStats,
    isMostEffective: Boolean,
    maxFocusTime: Long
) {
    val modeName = when (mode) {
        FocusMode.NORMAL -> "Normal Focus"
        FocusMode.EXTREME -> "Extreme Focus"
        FocusMode.ULTIMATE -> "Ultimate Focus"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(modeName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                    if (isMostEffective) {
                        Spacer(Modifier.width(8.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                "Most Effective",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
                
                val completionText = stats.completionRate?.let { "${(it * 100).toInt()}%" } ?: "No data"
                Text(
                    text = "Completion: $completionText",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            
            Spacer(Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatTotalTime(stats.focusTimeMillis),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${stats.completedSessions} sessions",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            
            Spacer(Modifier.height(12.dp))
            
            // Proportional horizontal bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(3.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(if (maxFocusTime > 0) stats.focusTimeMillis.toFloat() / maxFocusTime else 0f)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(3.dp))
                )
            }
        }
    }
}

@Composable
fun WeeklyDistributionChart(stats: FocusStats) {
    val maxFocusMillis = stats.focusMinutesPerDay.values.maxOrNull()?.coerceAtLeast(1L) ?: 1L
    val sortedDates = stats.focusMinutesPerDay.keys.sorted()
    val isLongRange = stats.selectedRange != StatsRange.SEVEN_DAYS

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        val scrollState = rememberScrollState()
        
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .height(150.dp)
                .fillMaxWidth()
                .then(if (isLongRange) Modifier.horizontalScroll(scrollState) else Modifier),
            horizontalArrangement = if (isLongRange) Arrangement.spacedBy(8.dp) else Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            sortedDates.forEach { date ->
                val focusMillis = stats.focusMinutesPerDay[date] ?: 0L
                Column(
                    modifier = if (isLongRange) Modifier.width(32.dp) else Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .height(110.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Box(
                            modifier = Modifier
                                .width(if (isLongRange) 8.dp else 12.dp)
                                .fillMaxHeight((focusMillis.toFloat() / maxFocusMillis.toFloat()).coerceIn(0.01f, 1f))
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                )
                        )
                    }
                    
                    Spacer(Modifier.height(12.dp))
                    
                    val label = if (isLongRange) {
                        // Only show labels for every 5th day or if it's the last day
                        val index = sortedDates.indexOf(date)
                        if (index % 5 == 0 || index == sortedDates.size - 1) {
                            date.dayOfMonth.toString()
                        } else ""
                    } else {
                        date.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.getDefault())
                    }

                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

private fun formatTotalTime(millis: Long): String {
    val totalMinutes = millis / 60000
    return when {
        totalMinutes < 60 -> "${totalMinutes} min"
        totalMinutes == 60L -> "1h"
        else -> {
            val h = totalMinutes / 60
            val m = totalMinutes % 60
            if (m == 0L) "${h}h" else "${h}h ${m}m"
        }
    }
}

private fun formatAverageTime(millis: Long): String {
    val totalMinutes = millis / 60000
    return when {
        totalMinutes == 0L -> "0 min/day"
        totalMinutes < 60 -> "${totalMinutes} min/day"
        else -> {
            val h = totalMinutes / 60
            val m = totalMinutes % 60
            if (m == 0L) "${h}h/day" else "${h}h ${m}m/day"
        }
    }
}

private fun formatStreak(days: Int): String {
    return "$days ${if (days == 1) "day" else "days"}"
}
