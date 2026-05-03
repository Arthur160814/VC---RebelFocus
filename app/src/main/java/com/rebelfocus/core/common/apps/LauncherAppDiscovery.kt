package com.rebelfocus.core.common.apps

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LauncherAppDiscovery @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun queryLauncherApps(): List<InstalledAppInfo> {
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val pm = context.packageManager
        val resolved: List<ResolveInfo> = pm.queryIntentActivities(intent, PackageManager.MATCH_ALL)
        val ownPackage = context.packageName
        return resolved
            .filter { it.activityInfo.packageName != ownPackage }
            .map {
                InstalledAppInfo(
                    packageName = it.activityInfo.packageName,
                    appName = it.loadLabel(pm).toString()
                )
            }
            .distinctBy { it.packageName }
            .sortedBy { it.appName.lowercase() }
    }
}
