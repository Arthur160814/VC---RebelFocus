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
import com.rebelfocus.core.model.StatsRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
                    val ranges = listOf(StatsRange.SEVEN_DAYS, StatsRange.THIRTY_DAYS, StatsRange.ALL)
                    val rangeLabels = listOf("7D", "30D", "All")
                    val selectedRange by viewModel.selectedRange.collectAsState()

                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ranges.forEachIndexed { index, range ->
                            SegmentedButton(
                                selected = range == selectedRange,
                                onClick = { viewModel.setRange(range) },
                                shape = SegmentedButtonDefaults.itemShape(index = index, count = ranges.size),
                                label = { Text(rangeLabels[index]) }
                            )
                        }
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
