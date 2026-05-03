package com.rebelfocus.features.onboarding

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rebelfocus.core.common.permissions.PermissionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("Rebel Focus Setup") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Step indicator
            LinearProgressIndicator(
                progress = { (state.step.ordinal + 1f) / OnboardingStep.entries.size },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(32.dp))

            when (state.step) {
                OnboardingStep.Welcome -> WelcomePage(onNext = { viewModel.nextStep() })
                OnboardingStep.Accessibility -> AccessibilityPage(
                    enabled = state.permissions.accessibilityEnabled,
                    onOpenSettings = {
                        context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                    },
                    onRefresh = { viewModel.refreshPermissions() },
                    onNext = { viewModel.nextStep() },
                    onBack = { viewModel.previousStep() }
                )
                OnboardingStep.Notifications -> NotificationPage(
                    enabled = state.permissions.notificationsEnabled,
                    onRefresh = { viewModel.refreshPermissions() },
                    onNext = { viewModel.nextStep() },
                    onBack = { viewModel.previousStep() }
                )
                OnboardingStep.Summary -> SummaryPage(
                    permissions = state.permissions,
                    onFinish = {
                        viewModel.completeOnboarding()
                        onComplete()
                    },
                    onBack = { viewModel.previousStep() }
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.WelcomePage(onNext: () -> Unit) {
    Spacer(Modifier.weight(1f))
    Text("Welcome to Rebel Focus", style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    Spacer(Modifier.height(16.dp))
    Text("A distraction-blocking app that helps you stay focused. " +
            "We need a few permissions to work properly.",
        style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
    Spacer(Modifier.weight(1f))
    Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) { Text("Get Started") }
}

@Composable
private fun ColumnScope.AccessibilityPage(
    enabled: Boolean, onOpenSettings: () -> Unit,
    onRefresh: () -> Unit, onNext: () -> Unit, onBack: () -> Unit
) {
    Text("Accessibility Service", style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(16.dp))
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("Why we need this", style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text("The Accessibility Service detects which app is in the foreground " +
                    "so Rebel Focus can block distracting apps during your focus sessions. " +
                    "We do not read screen content or keystrokes.",
                style = MaterialTheme.typography.bodyMedium)
        }
    }
    Spacer(Modifier.height(16.dp))
    PermissionStatusRow("Accessibility", enabled)
    Spacer(Modifier.height(16.dp))
    if (!enabled) {
        OutlinedButton(onClick = onOpenSettings, modifier = Modifier.fillMaxWidth()) {
            Text("Open Accessibility Settings")
        }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onRefresh) { Text("I've enabled it — refresh") }
    }
    Spacer(Modifier.weight(1f))
    NavigationRow(onBack = onBack, onNext = onNext, nextLabel = "Next")
}

@Composable
private fun ColumnScope.NotificationPage(
    enabled: Boolean, onRefresh: () -> Unit,
    onNext: () -> Unit, onBack: () -> Unit
) {
    val notifLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { onRefresh() }
    val context = LocalContext.current

    Text("Notifications", style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(16.dp))
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("Why we need this", style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text("Notifications keep you informed about your active focus session " +
                    "and remaining time. They also power the persistent foreground service " +
                    "notification required by Android.",
                style = MaterialTheme.typography.bodyMedium)
        }
    }
    Spacer(Modifier.height(16.dp))
    PermissionStatusRow("Notifications", enabled)
    Spacer(Modifier.height(16.dp))
    if (!enabled) {
        OutlinedButton(
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notifLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    context.startActivity(intent)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Grant Notification Permission") }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onRefresh) { Text("Refresh status") }
    }
    Spacer(Modifier.weight(1f))
    NavigationRow(onBack = onBack, onNext = onNext, nextLabel = "Next")
}

@Composable
private fun ColumnScope.SummaryPage(
    permissions: PermissionState, onFinish: () -> Unit, onBack: () -> Unit
) {
    Text("Setup Summary", style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(24.dp))
    PermissionStatusRow("Accessibility Service", permissions.accessibilityEnabled)
    Spacer(Modifier.height(8.dp))
    PermissionStatusRow("Notifications", permissions.notificationsEnabled)
    Spacer(Modifier.height(8.dp))
    PermissionStatusRow("Exact Alarms", permissions.exactAlarmsEnabled)
    Spacer(Modifier.height(24.dp))
    if (!permissions.allGranted) {
        Text("Some permissions are missing. You can grant them later from Settings.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    Spacer(Modifier.weight(1f))
    NavigationRow(onBack = onBack, onNext = onFinish, nextLabel = "Finish")
}

@Composable
private fun PermissionStatusRow(label: String, granted: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        Text(
            if (granted) "✓ Granted" else "✗ Not granted",
            color = if (granted) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun NavigationRow(onBack: () -> Unit, onNext: () -> Unit, nextLabel: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        TextButton(onClick = onBack) { Text("Back") }
        Button(onClick = onNext) { Text(nextLabel) }
    }
}
