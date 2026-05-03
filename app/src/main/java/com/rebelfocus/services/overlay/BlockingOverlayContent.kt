package com.rebelfocus.services.overlay

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import android.os.Vibrator
import android.os.VibrationEffect
import com.rebelfocus.core.data.repository.SessionRepository
import com.rebelfocus.core.data.repository.FocusProfileRepository
import com.rebelfocus.core.designsystem.RebelFocusTheme
import kotlinx.coroutines.delay

@Composable
fun BlockingOverlayContent(
    packageName: String,
    sessionRepository: SessionRepository,
    profileRepository: FocusProfileRepository,
    onEmergencyExit: () -> Unit
) {
    val session by sessionRepository.observeEnforcementSession().collectAsState(initial = null)
    val profile by produceState<com.rebelfocus.core.model.FocusProfile?>(initialValue = null, key1 = session?.profileId) {
        value = session?.profileId?.let { profileRepository.getById(it) }
    }

    val context = LocalContext.current
    val vibrator = remember { context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as Vibrator }
    
    // Trigger haptic on phase change
    LaunchedEffect(session?.state) {
        if (session != null) {
            println("EXTREME_DEBUG: [Overlay] Triggering Haptic for state: ${session?.state}")
            try {
                if (vibrator.hasVibrator()) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
                    } else {
                        @Suppress("DEPRECATION")
                        vibrator.vibrate(50)
                    }
                }
            } catch (e: Exception) {
                println("EXTREME_DEBUG: [Overlay] Haptic failed: ${e.message}")
            }
        }
    }

    var currentTime by remember { mutableLongStateOf(android.os.SystemClock.elapsedRealtime()) }
    val isExtreme = session?.isExtremeMode == true

    LaunchedEffect(session?.id, session?.state) {
        while (true) {
            currentTime = android.os.SystemClock.elapsedRealtime()
            delay(500)
        }
    }

    val remainingMillis = session?.let { s ->
        if (s.state == com.rebelfocus.core.model.SessionState.Break) {
            val breakElapsed = (currentTime - (s.startTimeElapsedRealtime ?: currentTime)).coerceAtLeast(0)
            (s.breakDurationMillis - breakElapsed).coerceAtLeast(0)
        } else {
            val focusPerInterval = s.plannedDurationMillis
            val focusElapsed = s.elapsedAtPauseMillis + if (s.state == com.rebelfocus.core.model.SessionState.ActiveFocus) {
                (currentTime - (s.startTimeElapsedRealtime ?: currentTime)).coerceAtLeast(0)
            } else 0L
            
            val elapsedThisInterval = if (s.pomodoroTarget > 1) {
                focusElapsed - (s.pomodoroCount.toLong() * focusPerInterval)
            } else {
                focusElapsed
            }
            (focusPerInterval - elapsedThisInterval).coerceAtLeast(0)
        }
    } ?: 0L

    val timerText = remainingMillis.let { remaining ->
        val minutes = remaining / 60000
        val seconds = (remaining % 60000) / 1000
        "%02d:%02d".format(minutes, seconds)
    }

    val progress = session?.let { s ->
        val total = if (s.state == com.rebelfocus.core.model.SessionState.Break) s.breakDurationMillis else s.plannedDurationMillis
        if (total > 0) (remainingMillis.toFloat() / total.toFloat()).coerceIn(0f, 1f) else 0f
    } ?: 0f
    var hasStarted by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        hasStarted = true
    }

    val introAlpha by animateFloatAsState(
        targetValue = if (hasStarted) 1f else 0f,
        animationSpec = tween(1200, easing = LinearOutSlowInEasing)
    )

    val contentScale by animateFloatAsState(
        targetValue = if (hasStarted) 1f else 0.92f,
        animationSpec = tween(1500, easing = EaseOutCubic)
    )

    val phaseColor by animateColorAsState(
        targetValue = when {
            session?.state == com.rebelfocus.core.model.SessionState.Break -> Color(0xFF1976D2) // Premium Blue
            session?.state == com.rebelfocus.core.model.SessionState.ActiveFocus -> Color(0xFFD32F2F) // Deep Red
            else -> Color(0xFF757575) // Neutral Gray for paused/other
        },
        animationSpec = tween(durationMillis = 800)
    )

    RebelFocusTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(introAlpha)
                .background(Color(0xFF050505))
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent()
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                if (session?.state == com.rebelfocus.core.model.SessionState.Completed) {
                    // Completion Surface
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "SESSION COMPLETE",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White.copy(alpha = 0.6f),
                            letterSpacing = 4.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(24.dp))
                        Text(
                            text = "Well Done",
                            style = MaterialTheme.typography.displayMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Light
                        )
                        Spacer(Modifier.height(48.dp))
                        Text(
                            text = "Focus: ${session?.plannedDurationMillis?.div(60000)}m",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.4f)
                        )
                        Text(
                            text = "Intervals: ${session?.pomodoroCount} / ${session?.pomodoroTarget}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.4f)
                        )
                        Spacer(Modifier.height(80.dp))
                        Button(
                            onClick = onEmergencyExit,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.1f),
                                contentColor = Color.White
                            ),
                            shape = MaterialTheme.shapes.extraSmall
                        ) {
                            Text("DONE", modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp), letterSpacing = 2.sp)
                        }
                    }
                } else {
                    // Intention Label
                    profile?.name?.let { name ->
                        Text(
                            text = name.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.2f),
                            letterSpacing = 2.sp,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    }

                    // Circular Progress + Timer
                    Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { 1f },
                        modifier = Modifier.size(300.dp),
                        color = Color.White.copy(alpha = 0.03f),
                        strokeWidth = 4.dp,
                        strokeCap = StrokeCap.Round
                    )
                    
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.size(300.dp),
                        color = phaseColor,
                        strokeWidth = 6.dp,
                        strokeCap = StrokeCap.Round
                    )
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.graphicsLayer(scaleX = contentScale, scaleY = contentScale)) {
                        AnimatedContent(
                            targetState = session?.state,
                            transitionSpec = {
                                fadeIn(tween(600)) togetherWith fadeOut(tween(600))
                            }
                        ) { state ->
                            Text(
                                text = when(state) {
                                    com.rebelfocus.core.model.SessionState.ActiveFocus -> "FOCUS"
                                    com.rebelfocus.core.model.SessionState.Break -> "BREAK"
                                    com.rebelfocus.core.model.SessionState.Paused -> "PAUSED"
                                    else -> "READY"
                                },
                                style = MaterialTheme.typography.labelLarge,
                                color = phaseColor,
                                letterSpacing = 6.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = timerText,
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = 72.sp,
                                fontWeight = FontWeight.Light,
                                letterSpacing = (-2).sp
                            ),
                            color = Color.White
                        )
                    }
                }

                Spacer(Modifier.height(80.dp))

                if (session?.pomodoroTarget ?: 0 > 1) {
                    Text(
                        text = "Interval ${session?.pomodoroCount} of ${session?.pomodoroTarget}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.4f),
                        letterSpacing = 1.sp
                    )
                }

                Spacer(Modifier.weight(1f))

                val isUltimate = session?.isUltimateMode == true

                if (!isUltimate) {
                    Button(
                        onClick = onEmergencyExit,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.padding(bottom = 16.dp),
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Text(
                            "EMERGENCY EXIT",
                            style = MaterialTheme.typography.labelSmall,
                            letterSpacing = 2.sp
                        )
                    }
                }
            }
        }
    }
}
}
