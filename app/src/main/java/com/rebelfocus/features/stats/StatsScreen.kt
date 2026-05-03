package com.rebelfocus.features.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
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
import com.rebelfocus.core.model.FocusStats
import java.time.format.TextStyle
import java.util.*

import com.rebelfocus.core.ui.components.EmptyStateView
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info

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
                    Text("Overview", style = MaterialTheme.typography.titleMedium)
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            label = "Total Time",
                            value = "${s.totalFocusTimeMillis / 3600000}h ${ (s.totalFocusTimeMillis % 3600000) / 60000}m",
                            icon = Icons.Default.Check,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "Sessions",
                            value = s.totalSessions.toString(),
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
                            label = "Current Streak",
                            value = "${s.currentStreak} days",
                            icon = Icons.Default.DateRange,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "Longest Streak",
                            value = "${s.longestStreak} days",
                            icon = Icons.Default.Star,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Spacer(Modifier.height(8.dp))
                    Text("Last 7 Days", style = MaterialTheme.typography.titleMedium)
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
fun WeeklyDistributionChart(stats: FocusStats) {
    val maxSessions = stats.sessionsPerDay.values.maxOrNull()?.coerceAtLeast(1) ?: 1
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .height(150.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            stats.sessionsPerDay.entries.sortedBy { it.key }.forEach { entry ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .width(12.dp)
                            .fillMaxHeight(entry.value.toFloat() / maxSessions)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                            )
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = entry.key.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.getDefault()),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
