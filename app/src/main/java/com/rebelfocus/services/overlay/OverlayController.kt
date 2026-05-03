package com.rebelfocus.services.overlay

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OverlayController @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionRepository: com.rebelfocus.core.data.repository.SessionRepository,
    private val profileRepository: com.rebelfocus.core.data.repository.FocusProfileRepository
) {
    private var windowManager: WindowManager? = null
    private var overlayView: ComposeView? = null
    private var lifecycleOwner: OverlayLifecycleOwner? = null
    private var isShowing = false
    private val mainScope = MainScope()

    init {
        // Fallback to application window manager, though it may lack tokens for some types.
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    /**
     * Updates the context used for window management.
     * Should be called with the AccessibilityService instance to provide the correct token.
     */
    fun updateContext(newContext: Context) {
        windowManager = newContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    /**
     * Shows the blocking overlay for the specified package.
     * Must be called from the main thread.
     */
    fun showOverlay(packageName: String, onEmergencyExit: () -> Unit) {
        mainScope.launch {
            if (isShowing) {
                return@launch
            }

            val view = ComposeView(context).apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    Box(modifier = Modifier.onGloballyPositioned { coords ->
                    }) {
                        BlockingOverlayContent(
                            packageName = packageName,
                            sessionRepository = sessionRepository,
                            profileRepository = profileRepository,
                            onEmergencyExit = onEmergencyExit
                        )
                    }
                }
            }

            val owner = OverlayLifecycleOwner().apply {
                onCreate()
                onStart()
                onResume()
            }

            view.setViewTreeLifecycleOwner(owner)
            view.setViewTreeViewModelStoreOwner(owner)
            view.setViewTreeSavedStateRegistryOwner(owner)

            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.CENTER
                // Add animations if needed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    alpha = 1.0f
                }
            }


            try {
                windowManager?.addView(view, params)
                overlayView = view
                lifecycleOwner = owner
                isShowing = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Hides the overlay if it is currently shown.
     */
    fun hideOverlay(reason: String = "unknown") {
        mainScope.launch {
            if (!isShowing) {
                return@launch
            }
            try {
                overlayView?.let { view ->
                    windowManager?.removeView(view)
                    lifecycleOwner?.apply {
                        onPause()
                        onStop()
                        onDestroy()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                overlayView = null
                lifecycleOwner = null
                isShowing = false
            }
        }
    }
}
