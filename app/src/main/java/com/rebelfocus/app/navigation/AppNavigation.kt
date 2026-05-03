package com.rebelfocus.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rebelfocus.core.data.datastore.UserPreferencesDataStore
import com.rebelfocus.features.apps.AppPickerScreen
import com.rebelfocus.features.dashboard.DashboardScreen
import com.rebelfocus.features.onboarding.OnboardingScreen
import com.rebelfocus.features.profiles.ProfileEditScreen
import com.rebelfocus.features.profiles.ProfileListScreen

object Routes {
    const val ONBOARDING = "onboarding"
    const val DASHBOARD = "dashboard"
    const val APP_PICKER = "app_picker"
    const val PROFILE_LIST = "profile_list"
    const val PROFILE_EDIT = "profile_edit?profileId={profileId}"
    const val AUTOMATION_LIST = "automation_list"
    const val AUTOMATION_EDIT = "automation_edit?ruleId={ruleId}"
    const val BACKUP = "backup"
    const val DIAGNOSTIC_LOGS = "diagnostic_logs"
    const val STATS = "stats"

    fun profileEdit(profileId: String? = null): String =
        if (profileId != null) "profile_edit?profileId=$profileId"
        else "profile_edit"

    fun automationEdit(ruleId: String? = null): String =
        if (ruleId != null) "automation_edit?ruleId=$ruleId"
        else "automation_edit"
}

@Composable
fun AppNavigation(preferencesDataStore: UserPreferencesDataStore) {
    val navController = rememberNavController()
    val onboardingCompleted by preferencesDataStore.isOnboardingCompleted.collectAsState(initial = null)

    // Wait for DataStore to load before deciding start destination
    val startDest = when (onboardingCompleted) {
        null -> return // still loading
        true -> Routes.DASHBOARD
        false -> Routes.ONBOARDING
    }

    NavHost(navController = navController, startDestination = startDest) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onNavigateToProfiles = { navController.navigate(Routes.PROFILE_LIST) },
                onNavigateToAppPicker = { navController.navigate(Routes.APP_PICKER) },
                onNavigateToAutomations = { navController.navigate(Routes.AUTOMATION_LIST) },
                onNavigateToBackup = { navController.navigate(Routes.BACKUP) },
                onNavigateToLogs = { navController.navigate(Routes.DIAGNOSTIC_LOGS) },
                onNavigateToStats = { navController.navigate(Routes.STATS) },
                onNavigateToProfileEdit = { profileId -> navController.navigate(Routes.profileEdit(profileId)) }
            )
        }

        composable(Routes.APP_PICKER) {
            AppPickerScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Routes.PROFILE_LIST) {
            ProfileListScreen(
                onNavigateToEdit = { profileId ->
                    navController.navigate(Routes.profileEdit(profileId))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.PROFILE_EDIT,
            arguments = listOf(navArgument("profileId") {
                type = NavType.StringType; nullable = true; defaultValue = null
            })
        ) {
            ProfileEditScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAppPicker = { navController.navigate(Routes.APP_PICKER) }
            )
        }

        composable(Routes.AUTOMATION_LIST) {
            com.rebelfocus.features.automation.AutomationListScreen(
                onNavigateUp = { navController.popBackStack() },
                onNavigateToEdit = { ruleId ->
                    navController.navigate(Routes.automationEdit(ruleId))
                }
            )
        }

        composable(
            route = Routes.AUTOMATION_EDIT,
            arguments = listOf(navArgument("ruleId") {
                type = NavType.StringType; nullable = true; defaultValue = null
            })
        ) {
            com.rebelfocus.features.automation.AutomationEditScreen(
                onNavigateUp = { navController.popBackStack() }
            )
        }

        composable(Routes.BACKUP) {
            com.rebelfocus.features.backup.BackupScreen(
                onNavigateUp = { navController.popBackStack() }
            )
        }

        composable(Routes.DIAGNOSTIC_LOGS) {
            com.rebelfocus.features.logging.DiagnosticLogScreen(
                onNavigateUp = { navController.popBackStack() }
            )
        }

        composable(Routes.STATS) {
            com.rebelfocus.features.stats.StatsScreen(
                onNavigateUp = { navController.popBackStack() }
            )
        }
    }
}
