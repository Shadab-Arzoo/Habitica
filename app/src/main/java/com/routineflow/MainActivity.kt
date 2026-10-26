package com.routineflow

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
 import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Checklist
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.LocalFlorist
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.SportsEsports
import androidx.compose.material.icons.rounded.ViewInAr
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.border
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.launch
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.routineflow.data.local.HabitEntity
import com.routineflow.data.local.TaskEntity
import com.routineflow.domain.ExistingTimeBlock
import com.routineflow.domain.PlannableTask
import com.routineflow.domain.PlannerEngine
import com.routineflow.domain.PlannerRequest
import com.routineflow.domain.PlannerResult
import com.routineflow.notifications.FocusNotificationHelper
import com.routineflow.notifications.ReminderReceiver
import com.routineflow.ui.OnboardingScreen
import com.routineflow.ui.OnboardingViewModel
import com.routineflow.ui.RoutineViewModel
import com.routineflow.ui.TodayUiState
import com.routineflow.ui.components.ClearPdfDockMotion
import com.routineflow.ui.components.GlassSurface
import com.routineflow.ui.theme.RoutineFlowTheme
import com.routineflow.ui.theme.LiquidGlassTokens
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ReminderReceiver.createChannel(this)
        setContent { RoutineFlowRoot() }
    }
}

@Composable
private fun RoutineFlowRoot() {
    val systemDarkMode = isSystemInDarkTheme()
    val onboardingViewModel: OnboardingViewModel = hiltViewModel()
    val onboarding by onboardingViewModel.state.collectAsStateWithLifecycle()
    val reducedMotion by onboardingViewModel.reducedMotion.collectAsStateWithLifecycle()
    val savedDarkTheme by onboardingViewModel.darkTheme.collectAsStateWithLifecycle()
    val appLockEnabled by onboardingViewModel.appLockEnabled.collectAsStateWithLifecycle()
    
    val isDarkMode = savedDarkTheme ?: systemDarkMode
    var isAuthenticated by remember { mutableStateOf(false) }

    RoutineFlowTheme(darkTheme = isDarkMode) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (appLockEnabled && !isAuthenticated) {
                AppLockScreen(
                    onAuthenticated = { isAuthenticated = true }
                )
            } else {
                when (onboarding.completed) {
                    true -> RoutineFlowApp(
                        darkMode = isDarkMode,
                        onThemeToggle = { onboardingViewModel.setDarkTheme(!isDarkMode) },
                        reducedMotion = reducedMotion,
                        onReducedMotionChange = onboardingViewModel::setReducedMotion,
                        appLockEnabled = appLockEnabled,
                        onAppLockChange = onboardingViewModel::setAppLockEnabled
                    )
                    false -> OnboardingScreen(onFinished = {})
                    null -> { /* Loading saved onboarding status on launch */ }
                }
            }
        }
    }
}

@Composable
private fun AppLockScreen(onAuthenticated: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val haptics = LocalHapticFeedback.current
    
    androidx.compose.runtime.LaunchedEffect(Unit) {
        val fragmentActivity = context as? FragmentActivity ?: return@LaunchedEffect
        val executor = ContextCompat.getMainExecutor(context)
        
        val biometricPrompt = BiometricPrompt(fragmentActivity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onAuthenticated()
                }
            })
            
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("RoutineFlow")
            .setSubtitle("Unlock to access your habits and tasks")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()
            
        biometricPrompt.authenticate(promptInfo)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Icon(
                Icons.Rounded.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                "App Locked",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            FilledTonalButton(
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    val fragmentActivity = context as? FragmentActivity ?: return@FilledTonalButton
                    val executor = ContextCompat.getMainExecutor(context)
                    val biometricPrompt = BiometricPrompt(fragmentActivity, executor,
                        object : BiometricPrompt.AuthenticationCallback() {
                            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                super.onAuthenticationSucceeded(result)
                                onAuthenticated()
                            }
                        })
                    val promptInfo = BiometricPrompt.PromptInfo.Builder()
                        .setTitle("RoutineFlow")
                        .setSubtitle("Unlock to access your habits and tasks")
                        .setAllowedAuthenticators(
                            BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                        )
                        .build()
                    biometricPrompt.authenticate(promptInfo)
                },
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text("Unlock", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun RoutineFlowApp(
    darkMode: Boolean,
    onThemeToggle: () -> Unit,
    reducedMotion: Boolean,
    onReducedMotionChange: (Boolean) -> Unit,
    appLockEnabled: Boolean,
    onAppLockChange: (Boolean) -> Unit,
    viewModel: RoutineViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    var showAdd by remember { mutableStateOf(false) }
    var showAddTask by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var avatarClickCount by remember { mutableIntStateOf(0) }
    var activeFocusTask by remember { mutableStateOf<TaskEntity?>(null) }
    var addWaterActive by remember { mutableStateOf(false) }
    var closingAddTask by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    val haptics = LocalHapticFeedback.current
    val addAnimationScope = androidx.compose.runtime.rememberCoroutineScope()
    val waterProgress = remember { androidx.compose.animation.core.Animatable(0f) }

    val context = androidx.compose.ui.platform.LocalContext.current
    val activity = context as? FragmentActivity
    androidx.compose.runtime.LaunchedEffect(activity?.intent) {
        activity?.intent?.action?.let { action ->
            if (action == "com.routineflow.ACTION_ADD_TASK") {
                showAddTask = true
                selectedTab = 1
                activity.intent.action = null
            } else if (action == "com.routineflow.ACTION_ADD_HABIT") {
                showAdd = true
                selectedTab = 0
                activity.intent.action = null
            }
        }
    }

    fun openAddTaskWithWater() {
        if (addWaterActive || showAddTask) return
        addAnimationScope.launch {
            addWaterActive = true
            closingAddTask = false
            waterProgress.snapTo(0f)
            if (reducedMotion) {
                waterProgress.snapTo(1f)
                showAddTask = true
                addWaterActive = false
            } else {
                launch {
                    waterProgress.animateTo(
                        1f,
                        animationSpec = spring(
                            dampingRatio = 0.58f,
                            stiffness = 260f
                        )
                    )
                }
                delay(220L)
                showAddTask = true
                delay(400L)
                addWaterActive = false
            }
        }
    }

    fun closeAddTaskWithWater() {
        if (!showAddTask && !addWaterActive) return
        addAnimationScope.launch {
            if (reducedMotion) {
                waterProgress.snapTo(0f)
                showAddTask = false
                addWaterActive = false
            } else {
                addWaterActive = true
                closingAddTask = true
                waterProgress.animateTo(
                    0f,
                    androidx.compose.animation.core.tween(
                        durationMillis = 520,
                        easing = androidx.compose.animation.core.FastOutSlowInEasing
                    )
                )
                showAddTask = false
                addWaterActive = false
            }
        }
    }

    Scaffold(
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.End,
        topBar = {
            val hour = LocalTime.now().hour
            val greeting = when (hour) {
                in 5..11 -> "Good morning"
                in 12..16 -> "Good afternoon"
                in 17..20 -> "Good evening"
                else -> "Good night"
            }

            val title = when (selectedTab) {
                0 -> greeting
                1 -> "Planner"
                2 -> "Garden"
                3 -> "Progress"
                4 -> "Focus"
                else -> "Settings"
            }

            val subtitle = when (selectedTab) {
                0 -> {
                    val date = LocalDate.now().format(DateTimeFormatter.ofPattern("MMM d", Locale.getDefault()))
                    val remainingTasks = tasks.count { !it.completed }
                    "Today is $date • $remainingTasks task${if (remainingTasks != 1) "s" else ""} left"
                }
                1 -> "Tasks & smart schedule"
                2 -> "Grow your daily tree"
                3 -> "Track your journey"
                4 -> "Deep work sessions"
                else -> "App preferences"
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                                MaterialTheme.colorScheme.background.copy(alpha = 0.0f)
                            )
                        )
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        AnimatedContent(
                            targetState = title,
                            transitionSpec = {
                                (fadeIn() + slideInVertically { it / 2 }) togetherWith (fadeOut() + slideOutVertically { -it / 2 })
                            }, label = "header_title"
                        ) { targetTitle ->
                            Text(
                                text = targetTitle,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        AnimatedContent(
                            targetState = subtitle,
                            transitionSpec = {
                                (fadeIn() + slideInHorizontally { it / 4 }) togetherWith (fadeOut() + slideOutHorizontally { -it / 4 })
                            }, label = "header_subtitle"
                        ) { targetSubtitle ->
                            Text(
                                text = targetSubtitle,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Click Animation & Progression States
                    var iconClickPulse by remember { mutableFloatStateOf(1f) }
                    val animatedClickPulse by animateFloatAsState(
                        targetValue = iconClickPulse,
                        animationSpec = spring(dampingRatio = 0.4f, stiffness = 400f),
                        label = "header_icon_click"
                    )
                    
                    var iconRotation by remember { mutableFloatStateOf(0f) }
                    val animatedIconRotation by animateFloatAsState(
                        targetValue = iconRotation,
                        animationSpec = spring(dampingRatio = 0.5f, stiffness = 150f),
                        label = "header_icon_rotation"
                    )

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .graphicsLayer {
                                scaleX = if (animatedClickPulse < 1.5f) animatedClickPulse else 1f
                                scaleY = if (animatedClickPulse < 1.5f) animatedClickPulse else 1f
                            }
                            .shadow(
                                elevation = 12.dp,
                                shape = CircleShape,
                                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            )
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.surface,
                                        MaterialTheme.colorScheme.surfaceContainerHighest
                                    )
                                )
                            )
                            .border(
                                width = 1.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.8f),
                                        Color.White.copy(alpha = 0.1f)
                                    )
                                ),
                                shape = CircleShape
                            )
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        iconRotation += dragAmount.x + dragAmount.y
                                    },
                                    onDragEnd = {
                                        iconRotation = kotlin.math.round(iconRotation / 360f) * 360f
                                    }
                                )
                            }
                            .clickable(
                                interactionSource = null,
                                indication = null
                            ) {
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                if (avatarClickCount < 5) {
                                    avatarClickCount++
                                }
                                if (avatarClickCount == 5) {
                                    showSettingsDialog = true
                                }
                                addAnimationScope.launch {
                                    iconClickPulse = 2f 
                                    kotlinx.coroutines.delay(50)
                                    iconClickPulse = 0.7f
                                    kotlinx.coroutines.delay(120)
                                    iconClickPulse = 1f
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        // Progressive Unlocking Radial Orbit Ring
                        val targetUnlocked = when (avatarClickCount) {
                            0 -> 0
                            1 -> 2
                            2 -> 4
                            else -> 5
                        }
                        
                        val arcRevealAnim = remember { androidx.compose.animation.core.Animatable(0f) }
                        val pulseAnim = remember { androidx.compose.animation.core.Animatable(0f) }
                        
                        androidx.compose.runtime.LaunchedEffect(avatarClickCount) {
                            arcRevealAnim.snapTo(0f)
                            arcRevealAnim.animateTo(
                                1f,
                                animationSpec = spring(
                                    dampingRatio = 0.7f,
                                    stiffness = 380f
                                )
                            )
                            if (avatarClickCount >= 3) {
                                pulseAnim.snapTo(0f)
                                pulseAnim.animateTo(
                                    1f,
                                    animationSpec = androidx.compose.animation.core.tween(
                                        durationMillis = 500,
                                        easing = androidx.compose.animation.core.FastOutSlowInEasing
                                    )
                                )
                                pulseAnim.snapTo(0f)
                            }
                        }

                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val center = Offset(size.width / 2f, size.height / 2f)
                            val radius = size.minDimension / 2f * 0.88f
                            val pPulse = pulseAnim.value
                            
                            // Subtle background track ring
                            drawCircle(
                                color = Color.White.copy(alpha = 0.12f),
                                radius = radius,
                                center = center,
                                style = Stroke(width = 1.5.dp.toPx())
                            )

                            val streams = listOf(
                                Pair(Color(0xFF38BDF8), 0f),       // Sky Cyan
                                Pair(Color(0xFF4ADE80), 72f),      // Emerald Green
                                Pair(Color(0xFF818CF8), 144f),     // Indigo
                                Pair(Color(0xFFFBBF24), 216f),     // Amber
                                Pair(Color(0xFFF472B6), 288f)      // Neon Pink
                            )
                            
                            if (targetUnlocked > 0) {
                                val activeStreams = streams.take(targetUnlocked)
                                
                                activeStreams.forEachIndexed { index, (color, startAngleOffset) ->
                                    val targetSweep = 50f
                                    val currentSweep = if (index >= targetUnlocked - 2) {
                                        targetSweep * arcRevealAnim.value
                                    } else {
                                        targetSweep
                                    }
                                    
                                    if (currentSweep > 0.5f) {
                                        drawArc(
                                            color = color,
                                            startAngle = startAngleOffset,
                                            sweepAngle = currentSweep,
                                            useCenter = false,
                                            topLeft = Offset(center.x - radius, center.y - radius),
                                            size = Size(radius * 2, radius * 2),
                                            style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round)
                                        )

                                        val headAngleRad = Math.toRadians((startAngleOffset + currentSweep).toDouble())
                                        val px = center.x + kotlin.math.cos(headAngleRad).toFloat() * radius
                                        val py = center.y + kotlin.math.sin(headAngleRad).toFloat() * radius
                                        
                                        val pulseExpansion = if (pPulse > 0f) kotlin.math.sin(pPulse * Math.PI).toFloat() * 4.5.dp.toPx() else 0f
                                        val haloRadius = 4.5.dp.toPx() + pulseExpansion
                                        val coreRadius = 2.0.dp.toPx() + (pulseExpansion * 0.35f)
                                        
                                        drawCircle(
                                            color = color.copy(alpha = 0.45f + (if (pPulse > 0f) (1f - pPulse) * 0.3f else 0f)),
                                            radius = haloRadius,
                                            center = Offset(px, py)
                                        )
                                        drawCircle(
                                            color = Color.White,
                                            radius = coreRadius,
                                            center = Offset(px, py)
                                        )
                                    }
                                }
                            }
                        }

                        AnimatedContent(
                            targetState = if (avatarClickCount >= 4) -1 else selectedTab,
                            transitionSpec = {
                                (fadeIn(animationSpec = androidx.compose.animation.core.tween(300)) + scaleIn(initialScale = 0.8f)) togetherWith 
                                (fadeOut(animationSpec = androidx.compose.animation.core.tween(200)) + scaleOut(targetScale = 0.8f))
                            }, label = "header_icon"
                        ) { tab ->
                            Icon(
                                imageVector = if (tab == -1) {
                                    Icons.Rounded.Settings
                                } else {
                                    when(tab) {
                                        0 -> Icons.Rounded.Today
                                        1 -> Icons.Rounded.Checklist
                                        2 -> Icons.Rounded.Event
                                        3 -> Icons.Rounded.LocalFlorist
                                        4 -> Icons.Rounded.Insights
                                        5 -> Icons.Rounded.SelfImprovement
                                        else -> Icons.Rounded.Settings
                                    }
                                },
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(26.dp)
                                    .graphicsLayer { rotationZ = animatedIconRotation }
                            )
                        }

                        if (selectedTab == 0 && tasks.any { !it.completed }) {
                            val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition()
                            // Expand the duration so it rests at 1.0f for most of the 4-second cycle
                            // then briefly flares to 1.4f
                            val pulseScale by infiniteTransition.animateFloat(
                                initialValue = 1f,
                                targetValue = 1f, // Default target
                                animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                                    animation = androidx.compose.animation.core.keyframes {
                                        durationMillis = 4000
                                        1f at 0
                                        1f at 3000 // Stay hidden/small for 3 seconds
                                        1.4f at 3700 using androidx.compose.animation.core.FastOutSlowInEasing // Pulse out over 700ms
                                        1f at 4000 // Fade out fully
                                    },
                                    repeatMode = androidx.compose.animation.core.RepeatMode.Restart
                                ),
                                label = "pulse"
                            )
                            
                            // Only draw the border when it's actively pulsing outward
                            if (pulseScale > 1.01f) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .graphicsLayer {
                                            scaleX = pulseScale
                                            scaleY = pulseScale
                                            alpha = (1.4f - pulseScale) / 0.4f * 0.5f
                                        }
                                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                )
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                var addPressed by remember { mutableStateOf(false) }
                val addScale by animateFloatAsState(
                    targetValue = when {
                        addPressed -> 0.9f
                        addWaterActive -> 0.94f
                        else -> 1f
                    },
                    animationSpec = if (reducedMotion) {
                        androidx.compose.animation.core.snap()
                    } else {
                        spring(dampingRatio = 0.58f, stiffness = 720f)
                    },
                    label = "add_button_scale"
                )
                val addLift by animateFloatAsState(
                    targetValue = if (addPressed) 2f else 0f,
                    animationSpec = if (reducedMotion) {
                        androidx.compose.animation.core.snap()
                    } else {
                        spring(dampingRatio = 0.62f, stiffness = 560f)
                    },
                    label = "add_button_lift"
                )
                Box(
                    modifier = Modifier
                        .offset(y = (-64).dp)
                        .padding(bottom = 8.dp)
                        .size(96.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    WaterDropletBurst(
                        progress = waterProgress.value,
                        closing = closingAddTask,
                        reducedMotion = reducedMotion,
                        modifier = Modifier.matchParentSize()
                    )
                    Box(
                        modifier = Modifier
                        .size(56.dp)
                        .graphicsLayer {
                            scaleX = addScale
                            scaleY = addScale
                            translationY = -addLift
                            rotationZ = if (addPressed) -2f else 0f
                        }
                        .shadow(
                            elevation = if (addPressed) 18.dp else 12.dp,
                            shape = CircleShape,
                            ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                            spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        )
                        .clip(CircleShape)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
                                )
                            )
                        )
                        .drawWithContent {
                            drawContent()
                            drawCircle(
                                color = Color.White.copy(alpha = if (addPressed) 0.28f else 0.16f),
                                radius = size.minDimension * 0.34f,
                                center = Offset(
                                    x = size.width * 0.32f,
                                    y = size.height * 0.24f
                                )
                            )
                        }
                        .border(
                            width = 1.dp,
                            brush = Brush.verticalGradient(
                                0.0f to Color.White.copy(alpha = 0.78f),
                                0.2f to Color.White.copy(alpha = 0.28f),
                                1.0f to Color.Black.copy(alpha = 0.24f)
                            ),
                            shape = CircleShape
                        )
                        .pointerInput(reducedMotion, addWaterActive) {
                            detectTapGestures(
                                onPress = {
                                    addPressed = true
                                    try {
                                        awaitRelease()
                                    } finally {
                                        addPressed = false
                                    }
                                },
                                onTap = {
                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    openAddTaskWithWater()
                                }
                            )
                        }
                        .semantics {
                            role = Role.Button
                            contentDescription = "Add a new task"
                        },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.08f))
                                .border(
                                    1.dp,
                                    Color.White.copy(alpha = 0.18f),
                                    CircleShape
                                )
                        )
                        Icon(
                            Icons.Rounded.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(28.dp)
                                .graphicsLayer {
                                    scaleX = if (addPressed || addWaterActive) 0.88f else 1f
                                    scaleY = if (addPressed || addWaterActive) 0.88f else 1f
                                    rotationZ = if (addPressed || addWaterActive) -8f else 0f
                                    alpha = 1f - waterProgress.value.coerceIn(0f, 1f)
                                }
                        )
                    }
                }
            }
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
        ) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    if (reducedMotion) {
                        fadeIn(androidx.compose.animation.core.tween(300)) togetherWith fadeOut(androidx.compose.animation.core.tween(300))
                    } else if (targetState > initialState) {
                        (slideInHorizontally { it / 4 } + fadeIn(androidx.compose.animation.core.tween(300)) + scaleIn(initialScale = 0.92f, animationSpec = androidx.compose.animation.core.tween(300))) togetherWith
                            (slideOutHorizontally { -it / 4 } + fadeOut(androidx.compose.animation.core.tween(300)) + scaleOut(targetScale = 0.92f, animationSpec = androidx.compose.animation.core.tween(300)))
                    } else {
                        (slideInHorizontally { -it / 4 } + fadeIn(androidx.compose.animation.core.tween(300)) + scaleIn(initialScale = 0.92f, animationSpec = androidx.compose.animation.core.tween(300))) togetherWith
                            (slideOutHorizontally { it / 4 } + fadeOut(androidx.compose.animation.core.tween(300)) + scaleOut(targetScale = 0.92f, animationSpec = androidx.compose.animation.core.tween(300)))
                    }
                },
                label = "screen_transition",
                modifier = Modifier.fillMaxSize()
            ) { tab ->
                when (tab) {
                    0 -> TodayScreen(
                        state = state,
                        tasks = tasks,
                        viewModel = viewModel,
                        onOpenAdd = { showAdd = true },
                        onOpenTask = { openAddTaskWithWater() },
                        onStartFocusOnTask = { task ->
                            activeFocusTask = task
                            selectedTab = 4
                        }
                    )
                    1 -> PlannerScreen(
                        tasks = tasks,
                        viewModel = viewModel,
                        onOpenTask = { openAddTaskWithWater() },
                        onStartFocusOnTask = { task ->
                            activeFocusTask = task
                            selectedTab = 4
                        }
                    )
                    2 -> GardenScreen(state, tasks)
                    3 -> ProgressScreen(state)
                    4 -> FocusScreen(
                        tasks = tasks,
                        activeFocusTask = activeFocusTask,
                        onClearFocusTask = { activeFocusTask = null }
                    )
                    else -> SettingsScreen(
                        darkMode = darkMode,
                        onThemeToggle = onThemeToggle,
                        reducedMotion = reducedMotion,
                        onReducedMotionChange = onReducedMotionChange,
                        appLockEnabled = appLockEnabled,
                        onAppLockChange = onAppLockChange
                    )
                }
            }

            RoutineFlowDockClearPdf(
                selectedTab = selectedTab,
                reducedMotion = reducedMotion,
                onTabSelected = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    selectedTab = it
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }

    if (showAdd) {
        AddHabitDialog(
            reducedMotion = reducedMotion,
            onDismiss = { showAdd = false },
            onAdd = { name, category, emoji, reminderMinutes ->
                viewModel.addHabit(name, category, emoji, reminderMinutes)
                showAdd = false
            }
        )
    }
    if (showAddTask) {
        AddTaskDialog(
            reducedMotion = reducedMotion,
            onDismiss = { closeAddTaskWithWater() },
            onAdd = { title, description, category, priority, estimate, dueAt ->
                viewModel.addTask(title, description, category, priority, dueAt, estimate)
                closeAddTaskWithWater()
            }
        )
    }
    if (showSettingsDialog) {
        Dialog(
            onDismissRequest = {
                showSettingsDialog = false
                avatarClickCount = 0
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.65f))
                    .clickable {
                        showSettingsDialog = false
                        avatarClickCount = 0
                    }
                    .padding(horizontal = 16.dp, vertical = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp,
                    shadowElevation = 16.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.88f)
                        .clickable(enabled = false) {}
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        SettingsScreen(
                            darkMode = darkMode,
                            onThemeToggle = onThemeToggle,
                            reducedMotion = reducedMotion,
                            onReducedMotionChange = onReducedMotionChange,
                            appLockEnabled = appLockEnabled,
                            onAppLockChange = onAppLockChange
                        )
                        IconButton(
                            onClick = {
                                showSettingsDialog = false
                                avatarClickCount = 0
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                        ) {
                            Icon(
                                Icons.Rounded.Close,
                                contentDescription = "Close Settings",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WaterDropletBurst(
    progress: Float,
    closing: Boolean,
    reducedMotion: Boolean,
    modifier: Modifier = Modifier
) {
    if (progress <= 0f) return
    val primaryColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier) {
        val t = progress.coerceIn(0f, 1f)
        val fade = if (closing) t else (1f - ((t - 0.65f) / 0.35f)).coerceIn(0f, 1f)
        val baseX = size.width / 2f
        val baseY = size.height * 0.75f
        val spread = size.width * 0.22f
        val height = size.height * 0.85f

        val droplets = listOf(
            Triple(-1.3f, 0.95f, 0.85f),
            Triple(-0.7f, 1.35f, 1.15f),
            Triple(-0.2f, 1.10f, 0.95f),
            Triple(0.2f, 1.45f, 1.30f),
            Triple(0.7f, 1.20f, 1.05f),
            Triple(1.25f, 0.90f, 0.80f),
            Triple(0.0f, 1.60f, 1.40f)
        )

        // Water Burst Ripple Wave
        val rippleRadius = size.width * (0.22f + 0.38f * t)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    primaryColor.copy(alpha = fade * 0.40f),
                    primaryColor.copy(alpha = fade * 0.15f),
                    Color.Transparent
                ),
                center = Offset(baseX, baseY),
                radius = rippleRadius
            ),
            radius = rippleRadius,
            center = Offset(baseX, baseY)
        )

        // Micro splash spray
        val microCount = 6
        for (m in 0 until microCount) {
            val angle = -Math.PI / 6.0 - (m * Math.PI / 7.0)
            val microDistance = t * size.width * (0.18f + 0.14f * (m % 3))
            val mx = baseX + kotlin.math.cos(angle).toFloat() * microDistance
            val my = baseY + kotlin.math.sin(angle).toFloat() * microDistance
            val microRadius = (2.0f + (m % 2) * 1.4f) * (1f - t)
            drawCircle(
                color = Color.White.copy(alpha = fade * 0.75f * (1f - t)),
                radius = microRadius,
                center = Offset(mx, my)
            )
        }

        // Calculate positions for metaball liquid bridges
        val computedSpheres = droplets.mapIndexed { index, (direction, arc, sizeFactor) ->
            val localT = (t * (0.88f + index * 0.03f)).coerceIn(0f, 1f)
            val x = baseX + direction * spread * localT + kotlin.math.sin(localT * Math.PI).toFloat() * direction * 12f
            val y = baseY - height * arc * localT + kotlin.math.sin(localT * Math.PI).toFloat() * 6f
            val radius = (6f + sizeFactor * 4f) * if (reducedMotion) 0.8f else 1f
            val alpha = fade * (1f - index * 0.05f)
            Triple(Offset(x, y), radius, alpha)
        }

        // Connect nearby liquid droplets with metaball bridges
        for ((i, s1) in computedSpheres.withIndex()) {
            for ((j, s2) in computedSpheres.withIndex()) {
                if (j <= i) continue
                val p1 = s1.first
                val p2 = s2.first
                val r1 = s1.second
                val r2 = s2.second
                val dist = (p1 - p2).getDistance()
                if (dist > 0.1f && dist < (r1 + r2) * 2.8f) {
                    val bridgeAlpha = (1f - dist / ((r1 + r2) * 2.8f)) * s1.third * 0.45f
                    if (bridgeAlpha > 0f) {
                        drawLine(
                            color = primaryColor.copy(alpha = bridgeAlpha),
                            start = p1,
                            end = p2,
                            strokeWidth = (r1 + r2) * 0.35f * (1f - dist / ((r1 + r2) * 2.8f)),
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
        }

        // 3D Glass Water Spheres
        computedSpheres.forEach { (center, radius, alpha) ->
            drawGlassWaterSphere(
                center = center,
                radius = radius,
                alpha = alpha,
                primaryColor = primaryColor
            )
        }
    }
}

private fun DrawScope.drawGlassWaterSphere(
    center: Offset,
    radius: Float,
    alpha: Float,
    primaryColor: Color
) {
    if (radius <= 0f || alpha <= 0f) return

    // Soft Ambient Water Glow
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                primaryColor.copy(alpha = alpha * 0.40f),
                primaryColor.copy(alpha = alpha * 0.10f),
                Color.Transparent
            ),
            center = center,
            radius = radius * 2.2f
        ),
        radius = radius * 2.2f,
        center = center
    )

    // Liquid Glass Body Fill
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color.White.copy(alpha = alpha * 0.60f),
                primaryColor.copy(alpha = alpha * 0.75f),
                primaryColor.copy(alpha = alpha * 0.35f)
            ),
            center = Offset(center.x - radius * 0.3f, center.y - radius * 0.3f),
            radius = radius * 1.2f
        ),
        radius = radius,
        center = center
    )

    // Specular Light Reflection Border Ring
    drawCircle(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = alpha * 0.85f),
                Color.White.copy(alpha = alpha * 0.15f),
                Color.Transparent
            ),
            start = Offset(center.x - radius, center.y - radius),
            end = Offset(center.x + radius, center.y + radius)
        ),
        radius = radius,
        center = center,
        style = Stroke(width = radius * 0.18f)
    )

    // Caustic reflection curve (bottom right)
    drawArc(
        color = Color.White.copy(alpha = alpha * 0.45f),
        startAngle = 10f,
        sweepAngle = 70f,
        useCenter = false,
        topLeft = Offset(center.x - radius * 0.85f, center.y - radius * 0.85f),
        size = Size(radius * 1.7f, radius * 1.7f),
        style = Stroke(width = radius * 0.15f, cap = StrokeCap.Round)
    )

    // Specular Highlight Spot
    drawOval(
        color = Color.White.copy(alpha = alpha * 0.95f),
        topLeft = Offset(center.x - radius * 0.55f, center.y - radius * 0.65f),
        size = Size(radius * 0.5f, radius * 0.25f)
    )
    
    // Secondary Micro Highlight
    drawCircle(
        color = Color.White.copy(alpha = alpha * 0.8f),
        radius = radius * 0.08f,
        center = Offset(center.x - radius * 0.15f, center.y - radius * 0.70f)
    )
}

@Composable
private fun RoutineFlowDockClearPdf(
    selectedTab: Int,
    reducedMotion: Boolean,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val labels = listOf("Today", "Planner", "Garden", "Progress", "Focus")
    val icons = listOf(
        Icons.Rounded.Today,
        Icons.Rounded.Event,
        Icons.Rounded.LocalFlorist,
        Icons.Rounded.Insights,
        Icons.Rounded.SelfImprovement
    )
    val density = LocalDensity.current
    val haptics = LocalHapticFeedback.current
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    val motion = remember(reducedMotion) {
        ClearPdfDockMotion(scope, selectedTab, labels.lastIndex, reducedMotion)
    }
    var widthPx by remember { mutableFloatStateOf(0f) }
    var pressed by remember { mutableStateOf(false) }
    val slotPx = widthPx / labels.size
    val panelOffset = remember { androidx.compose.animation.core.Animatable(0f) }

    // Tap-Beam State
    var beamTrigger by remember { mutableIntStateOf(0) }
    var beamOriginLeft by remember { mutableStateOf(true) }
    var beamTargetIndex by remember { mutableIntStateOf(0) }
    val beamProgress = remember { androidx.compose.animation.core.Animatable(0f) }

    androidx.compose.runtime.LaunchedEffect(beamTrigger) {
        if (beamTrigger > 0 && !reducedMotion) {
            beamProgress.snapTo(0f)
            beamProgress.animateTo(
                1f,
                animationSpec = androidx.compose.animation.core.tween(
                    durationMillis = 680,
                    easing = androidx.compose.animation.core.CubicBezierEasing(0.16f, 1f, 0.3f, 1f)
                )
            )
            beamProgress.snapTo(0f)
        }
    }

    androidx.compose.runtime.LaunchedEffect(selectedTab) {
        if (!pressed) motion.sync(selectedTab)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(92.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
            val dockGlowAlpha = if (beamProgress.value > 0f) {
                val p = beamProgress.value
                (if (p < 0.5f) p * 2f else (1f - p) * 2f).coerceIn(0f, 1f) * 0.5f
            } else 0f

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(72.dp)
                    .graphicsLayer {
                        scaleX = 1f + motion.pressed * 0.025f
                        scaleY = 1f + motion.pressed * 0.015f
                        translationX = panelOffset.value
                    }
                    .shadow(
                        elevation = (10 + dockGlowAlpha * 12).dp,
                        shape = CircleShape,
                        ambientColor = Color(0xFF38BDF8).copy(alpha = 0.2f + dockGlowAlpha * 0.5f),
                        spotColor = Color(0xFF818CF8).copy(alpha = 0.2f + dockGlowAlpha * 0.5f)
                    )
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.80f),
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
                            )
                        )
                    )
                    .border(
                        width = (1f + dockGlowAlpha * 2.5f).dp,
                        brush = Brush.verticalGradient(
                            0f to Color.White.copy(alpha = 0.65f + dockGlowAlpha * 0.35f),
                            0.3f to Color(0xFF38BDF8).copy(alpha = 0.15f + dockGlowAlpha * 0.7f),
                            1f to Color(0xFF818CF8).copy(alpha = 0.05f + dockGlowAlpha * 0.5f)
                        ),
                        shape = CircleShape
                    )
                .onSizeChanged { widthPx = it.width.toFloat() }
                .pointerInput(slotPx, reducedMotion) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        if (slotPx <= 0f) return@awaitEachGesture
                        pressed = true
                        val touchedTab = (down.position.x / slotPx - 0.5f).let { kotlin.math.round(it).toInt() }
                            .coerceIn(0, labels.lastIndex)
                        motion.startAt(touchedTab.toFloat())
                        motion.press()
                        var previousX = down.position.x
                        var moved = false
                        var released = false
                        while (!released) {
                            val event = awaitPointerEvent()
                            event.changes.forEach { change ->
                                if (!change.pressed) {
                                    released = true
                                } else {
                                    val deltaX = change.position.x - previousX
                                    previousX = change.position.x
                                    if (kotlin.math.abs(change.position.x - down.position.x) > 8f) {
                                        moved = true
                                        motion.dragBy(deltaX, slotPx)
                                        change.consume()
                                    }
                                }
                            }
                        }
                        pressed = false
                        val finalTab = if (!moved) touchedTab else motion.release()
                        if (!moved) {
                            motion.select(finalTab)
                            if (finalTab != selectedTab) {
                                beamOriginLeft = finalTab <= labels.lastIndex / 2
                                beamTargetIndex = finalTab
                                beamTrigger++
                            }
                        }
                        scope.launch {
                            panelOffset.animateTo(
                                0f,
                                if (reducedMotion) {
                                    spring(dampingRatio = 1f, stiffness = Spring.StiffnessMedium)
                                } else {
                                    spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium)
                                }
                            )
                        }
                        onTabSelected(finalTab)
                    }
                },
            contentAlignment = Alignment.CenterStart
        ) {
            if (slotPx > 0f) {
                val indicatorWidth = slotPx * 0.86f
                val indicatorIndex = motion.value
                Box(
                    modifier = Modifier
                        .width(with(density) { indicatorWidth.toDp() })
                        .fillMaxHeight()
                        .graphicsLayer {
                            translationX = slotPx * (indicatorIndex + 0.5f) - indicatorWidth / 2f
                            scaleX = motion.stretchX
                            scaleY = motion.stretchY
                        }
                        .padding(vertical = 7.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                )
                            )
                        )
                        .border(
                            1.dp,
                            Brush.verticalGradient(
                                0f to MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                0.5f to MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                1f to Color.Transparent
                            ),
                            CircleShape
                        )
                )
            }
            
            if (slotPx > 0f && beamProgress.value > 0f) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val progress = beamProgress.value
                    // Graceful fade out decay curve as progress nears 1.0
                    val globalAlpha = if (progress < 0.65f) 1f else ((1f - progress) / 0.35f).coerceIn(0f, 1f)
                    val targetX = slotPx * (beamTargetIndex + 0.5f)
                    val startX = if (beamOriginLeft) 0f else size.width
                    val midY = size.height / 2f

                    // 0. Background Radial Glass Wash
                    val washX = androidx.compose.ui.util.lerp(startX, targetX, progress)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF38BDF8).copy(alpha = 0.28f * globalAlpha),
                                Color(0xFF818CF8).copy(alpha = 0.12f * globalAlpha),
                                Color.Transparent
                            ),
                            center = Offset(washX, midY),
                            radius = 65.dp.toPx()
                        ),
                        radius = 65.dp.toPx(),
                        center = Offset(washX, midY)
                    )

                    // Multi-Stream Energy Trails (Top, Center, Bottom)
                    val streams = listOf(
                        Triple(-10.dp.toPx(), 0.90f, Color(0xFF38BDF8)), // Top Sky Cyan Stream
                        Triple(0.dp.toPx(), 1.00f, Color(0xFF818CF8)),   // Center Indigo Stream
                        Triple(10.dp.toPx(), 1.10f, Color(0xFFF472B6))   // Bottom Neon Pink Stream
                    )

                    streams.forEach { (yOffset, speedMult, accentColor) ->
                        val effectiveProgress = (progress * speedMult).coerceIn(0f, 1f)
                        // Arching parabola trajectory: arc rises in the middle of the flight
                        val arcHeight = kotlin.math.sin(effectiveProgress * Math.PI).toFloat() * 14.dp.toPx()
                        val currentHeadX = androidx.compose.ui.util.lerp(startX, targetX, effectiveProgress)
                        val tailLength = 220f
                        val tailX = if (beamOriginLeft) currentHeadX - tailLength else currentHeadX + tailLength

                        val colors = if (beamOriginLeft) {
                            listOf(Color.Transparent, accentColor.copy(alpha = 0.85f * globalAlpha), Color.White.copy(alpha = globalAlpha))
                        } else {
                            listOf(Color.White.copy(alpha = globalAlpha), accentColor.copy(alpha = 0.85f * globalAlpha), Color.Transparent)
                        }
                        val startGradientX = if (beamOriginLeft) tailX else currentHeadX
                        val endGradientX = if (beamOriginLeft) currentHeadX else tailX
                        val streamY = midY + yOffset - arcHeight

                        // 1. Moving Light Orb Ambient Aura
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    accentColor.copy(alpha = 0.50f * globalAlpha),
                                    accentColor.copy(alpha = 0.18f * globalAlpha),
                                    Color.Transparent
                                ),
                                center = Offset(currentHeadX, streamY),
                                radius = 32.dp.toPx()
                            ),
                            radius = 32.dp.toPx(),
                            center = Offset(currentHeadX, streamY)
                        )

                        // 2. Wide Outer Gradient Glow Line
                        drawLine(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, accentColor.copy(alpha = 0.55f * globalAlpha), Color.White.copy(alpha = 0.75f * globalAlpha)),
                                startX = startGradientX,
                                endX = endGradientX
                            ),
                            start = Offset(tailX, streamY),
                            end = Offset(currentHeadX, streamY),
                            strokeWidth = 18.dp.toPx(),
                            cap = StrokeCap.Round
                        )

                        // 3. Core Intense Energy Beam
                        drawLine(
                            brush = Brush.horizontalGradient(
                                colors = colors,
                                startX = startGradientX,
                                endX = endGradientX
                            ),
                            start = Offset(tailX, streamY),
                            end = Offset(currentHeadX, streamY),
                            strokeWidth = 3.8.dp.toPx(),
                            cap = StrokeCap.Round
                        )

                        // 4. Trailing Liquid Water Droplets
                        for (d in 1..3) {
                            val dropDist = d * 38f
                            val dropX = if (beamOriginLeft) currentHeadX - dropDist else currentHeadX + dropDist
                            val dropY = streamY + kotlin.math.sin(effectiveProgress * 12.0 + d).toFloat() * 5f
                            drawCircle(
                                color = Color.White.copy(alpha = 0.85f * globalAlpha * (1f - d * 0.25f)),
                                radius = (3.5f - d * 0.8f).dp.toPx().coerceAtLeast(1f),
                                center = Offset(dropX, dropY)
                            )
                        }
                    }

                    // Target Impact Energy Ring & 6-Point Water Splash Burst
                    if (progress > 0.50f) {
                        val burstProgress = ((progress - 0.50f) / 0.50f).coerceIn(0f, 1f)
                        val burstAlpha = (1f - burstProgress) * globalAlpha
                        val burstRadius = (12.dp.toPx() + 32.dp.toPx() * burstProgress)

                        // Outer Glowing Burst Ring
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF38BDF8).copy(alpha = burstAlpha * 0.75f),
                                    Color(0xFF818CF8).copy(alpha = burstAlpha * 0.35f),
                                    Color.Transparent
                                ),
                                center = Offset(targetX, midY),
                                radius = burstRadius * 1.8f
                            ),
                            radius = burstRadius * 1.8f,
                            center = Offset(targetX, midY)
                        )

                        // 6 Radial Splash Particles
                        for (s in 0 until 6) {
                            val splashAngle = s * (Math.PI / 3.0) + progress * 2.5
                            val splashDist = burstRadius * 1.1f
                            val sx = targetX + kotlin.math.cos(splashAngle).toFloat() * splashDist
                            val sy = midY + kotlin.math.sin(splashAngle).toFloat() * splashDist
                            drawCircle(
                                color = Color(0xFF38BDF8).copy(alpha = burstAlpha * 0.85f),
                                radius = 2.8.dp.toPx() * (1f - burstProgress),
                                center = Offset(sx, sy)
                            )
                        }

                        // Center Impact Glint
                        drawCircle(
                            color = Color.White.copy(alpha = burstAlpha * 0.95f),
                            radius = 8.dp.toPx() * (1f - burstProgress),
                            center = Offset(targetX, midY)
                        )
                    }
                }
            }
            
            Row(Modifier.fillMaxSize()) {
                labels.forEachIndexed { index, label ->
                    val highlighted = if (pressed) {
                        index == kotlin.math.round(motion.value).toInt().coerceIn(labels.indices)
                    } else {
                        index == selectedTab
                    }

                    val contentScale by animateFloatAsState(
                        targetValue = if (highlighted && pressed) 1.25f else 1f,
                        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
                        label = "magnifyIcon"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = null,
                                indication = null,
                                role = Role.Tab
                            ) {
                                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                motion.select(index)
                                onTabSelected(index)
                            }
                            .semantics {
                                contentDescription = "$label tab${if (index == selectedTab) ", selected" else ""}"
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.graphicsLayer {
                                scaleX = contentScale
                                scaleY = contentScale
                            },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                imageVector = icons[index],
                                contentDescription = null,
                                tint = if (highlighted) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                fontWeight = if (highlighted) FontWeight.Bold else FontWeight.Medium,
                                color = if (highlighted) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdaptiveLazyColumn(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    verticalArrangement: Arrangement.Vertical,
    content: LazyListScope.() -> Unit
) {
    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    
    Box(modifier = modifier.fillMaxSize()) {
        val contentWidth = when {
            screenWidth >= 1000.dp -> LiquidGlassTokens.wideContentWidth
            screenWidth >= 600.dp -> LiquidGlassTokens.compactContentWidth
            else -> screenWidth
        }
        LazyColumn(
            modifier = Modifier
                .width(contentWidth)
                .align(Alignment.TopCenter),
            contentPadding = PaddingValues(
                start = if (screenWidth >= 600.dp) contentPadding.calculateLeftPadding(androidx.compose.ui.unit.LayoutDirection.Ltr) + 8.dp else contentPadding.calculateLeftPadding(androidx.compose.ui.unit.LayoutDirection.Ltr),
                end = if (screenWidth >= 600.dp) contentPadding.calculateRightPadding(androidx.compose.ui.unit.LayoutDirection.Ltr) + 8.dp else contentPadding.calculateRightPadding(androidx.compose.ui.unit.LayoutDirection.Ltr),
                top = contentPadding.calculateTopPadding(),
                bottom = androidx.compose.ui.unit.max(contentPadding.calculateBottomPadding(), 120.dp)
            ),
            verticalArrangement = verticalArrangement,
            content = content
        )
    }
}

@Composable
private fun TodayScreen(
    state: TodayUiState,
    tasks: List<TaskEntity>,
    viewModel: RoutineViewModel,
    onOpenAdd: () -> Unit,
    onOpenTask: () -> Unit,
    onStartFocusOnTask: ((TaskEntity) -> Unit)? = null
) {
    AdaptiveLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { DailyTreeCard(state, tasks) }
        item { MomentumCard(state) }
        item { TaskSection(tasks, viewModel, onOpenTask, onStartFocusOnTask) }
        item {
            AnimatedVisibility(
                visible = state.recoveryCount > 0,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                RecoveryCard()
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your habits",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                TextButton(onClick = onOpenAdd) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Add habit")
                }
            }
        }
        item {
            AnimatedVisibility(
                visible = state.habits.isEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                GlassSurface(
                    modifier = Modifier.fillMaxWidth().clickable(onClick = onOpenAdd),
                    cornerRadius = LiquidGlassTokens.cardRadius,
                    elevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition()
                        val pulseScale by infiniteTransition.animateFloat(
                            initialValue = 0.95f,
                            targetValue = 1.05f,
                            animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                                animation = androidx.compose.animation.core.tween(1500, easing = androidx.compose.animation.core.FastOutSlowInEasing),
                                repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                            ),
                            label = "empty_icon_pulse"
                        )
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier
                                .size(48.dp)
                                .graphicsLayer {
                                    scaleX = pulseScale
                                    scaleY = pulseScale
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Rounded.Add,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                        Text(
                            text = "Build your first rhythm",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Tap here or the + button below to add your first habit.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        items(state.habits, key = { it.id }) { habit ->
            Box(Modifier.animateItem()) {
                HabitCard(
                    habit = habit,
                    completed = habit.id in state.completedIds,
                    onComplete = { viewModel.complete(habit.id) },
                    onArchive = { viewModel.archive(habit.id) }
                )
            }
        }
    }
}

/**
 * Interactive Daily Tree Card on the Today Screen.
 * Renders a low-poly 3D tree canvas that grows as tasks/habits are completed!
 */
@Composable
private fun DailyTreeCard(state: TodayUiState, tasks: List<TaskEntity>) {
    val completedHabits = state.completedCount
    val completedTasks = tasks.count { it.completed }
    val totalItems = (state.habits.size + tasks.size).coerceAtLeast(1)
    val completedItems = completedHabits + completedTasks
    val waterDrops = (completedHabits * 25) + (completedTasks * 20)
    val growthRatio = (completedItems.toFloat() / totalItems).coerceIn(0f, 1f)

    val animatedGrowth by animateFloatAsState(
        targetValue = growthRatio,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "tree_growth"
    )

    GlassSurface(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = LiquidGlassTokens.cardRadius,
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                            Text(
                                text = "Today's Sacred Tree",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                    Text(
                        text = when {
                            growthRatio >= 1.0f -> "100% All tasks done - Blooming!"
                            growthRatio >= 0.75f -> "75% Almost there - Large Tree"
                            growthRatio >= 0.50f -> "50% Half-grown Tree"
                            growthRatio >= 0.25f -> "25% Small Sprout"
                            else -> "0% No tasks done yet"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition()
                        val floatOffset by infiniteTransition.animateFloat(
                            initialValue = -2f,
                            targetValue = 2f,
                            animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                                animation = androidx.compose.animation.core.tween(1500, easing = androidx.compose.animation.core.FastOutSlowInEasing),
                                repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                            ),
                            label = "water_drop_float"
                        )
                        Icon(
                            Icons.Rounded.WaterDrop,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(16.dp)
                                .offset(y = floatOffset.dp)
                        )
                        Text(
                            text = "$waterDrops Drops",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Low-Poly 3D Tree Canvas Display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                contentAlignment = Alignment.Center
            ) {
                val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition()
                val timeFloat by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = (2 * Math.PI).toFloat(),
                    animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                        animation = androidx.compose.animation.core.tween(4000, easing = androidx.compose.animation.core.LinearEasing),
                        repeatMode = androidx.compose.animation.core.RepeatMode.Restart
                    ),
                    label = "magic_particles"
                )

                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val centerX = size.width / 2f
                    val centerY = size.height * 0.82f

                    drawIsoTile(centerX, centerY, 140f, 75f, Color(0xFF4ADE80), false)
                    if (animatedGrowth > 0f) {
                        drawLowPoly3DTree(centerX, centerY, 1.4f, animatedGrowth, timeFloat)
                    } else {
                        drawLowPolyRock(centerX, centerY)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = { animatedGrowth },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }
}

@Composable
private fun GardenDayChip(
    day: LocalDate,
    isToday: Boolean,
    isSelected: Boolean,
    dayRatio: Float,
    waterCollected: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dayOfWeek = if (isToday) "Today" else day.format(DateTimeFormatter.ofPattern("EEE", Locale.getDefault()))
    val dayNum = day.dayOfMonth.toString()

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = when {
            isSelected -> MaterialTheme.colorScheme.primary
            isToday -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
            else -> MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f)
        },
        border = BorderStroke(
            width = if (isSelected || isToday) 1.5.dp else 0.5.dp,
            color = when {
                isSelected -> Color.White.copy(alpha = 0.6f)
                isToday -> MaterialTheme.colorScheme.primary
                else -> Color.White.copy(alpha = 0.12f)
            }
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = dayOfWeek,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Medium,
                color = when {
                    isSelected -> MaterialTheme.colorScheme.onPrimary
                    isToday -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                },
                maxLines = 1
            )

            Text(
                text = dayNum,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )

            if (dayRatio > 0f) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) Color.White.copy(alpha = 0.25f) 
                            else Color(0xFF4ADE80).copy(alpha = 0.25f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Rounded.LocalFlorist,
                        contentDescription = "Growth",
                        tint = if (isSelected) Color.White else Color(0xFF4ADE80),
                        modifier = Modifier.size(13.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

/**
 * Dedicated Interactive Gaming Section (Sacred Tree Garden).
 * Features Low-Poly 3D Trees drawn directly on Canvas tiles for 2.5D Isometric & 3D Perspective!
 */
@Composable
private fun GardenScreen(state: TodayUiState, tasks: List<TaskEntity>) {
    val haptics = LocalHapticFeedback.current
    var is3DMode by remember { mutableStateOf(false) }
    var tiltAngle by remember { mutableFloatStateOf(0.45f) }
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    var zoomScale by remember { mutableFloatStateOf(1f) }
    var selectedDayIndex by remember { mutableIntStateOf(27) }
    val today = LocalDate.now()
    val pastDays = remember(today) { (27 downTo 0).map { today.minusDays(it.toLong()) } }

    AdaptiveLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GlassSurface(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = LiquidGlassTokens.cardRadius,
                elevation = 10.dp
            ) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                            Text(
                                "Daily Growth Garden",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Complete tasks to collect water and grow today's tree.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        val growthDays = pastDays.count { (state.taskActivity[it] ?: 0) > 0 }
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "$growthDays",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.width(4.dp))
                                Icon(
                                    Icons.Rounded.LocalFlorist,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            Text(
                                text = if (growthDays == 1) "day of growth" else "days of growth",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // The View Mode Switcher has been replaced by floating icon toggles.
                }
            }
        }

        // Low-Poly Garden Canvas
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Box {
                    val primaryColor = MaterialTheme.colorScheme.primary

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    ) {
                        val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition()
                    val timeFloat by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = (2 * Math.PI).toFloat(),
                        animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                            animation = androidx.compose.animation.core.tween(4000, easing = androidx.compose.animation.core.LinearEasing),
                            repeatMode = androidx.compose.animation.core.RepeatMode.Restart
                        ),
                        label = "forest_magic_particles"
                    )

                    Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(is3DMode) {
                                    detectTransformGestures { _, pan, zoom, _ ->
                                        zoomScale = (zoomScale * zoom).coerceIn(0.6f, 2.5f)
                                        if (is3DMode) {
                                            rotationAngle = (rotationAngle + pan.x * 0.35f) % 360f
                                            tiltAngle = (tiltAngle - pan.y / size.height * 1.2f).coerceIn(0.1f, 1.0f)
                                        }
                                    }
                                }
                                .pointerInput(is3DMode, pastDays) {
                                    detectTapGestures(
                                        onDoubleTap = {
                                            zoomScale = 1f
                                            if (is3DMode) {
                                                rotationAngle = 0f
                                                tiltAngle = 0.45f
                                            }
                                        },
                                        onTap = { tapOffset ->
                                            val canvasWidth = size.width.toFloat()
                                            val canvasHeight = size.height.toFloat()
                                            val tileWidth = canvasWidth / 5f
                                            var closestIndex = -1
                                            var minDistanceSq = Float.MAX_VALUE

                                            if (is3DMode) {
                                                val cameraYaw = Math.toRadians((rotationAngle + 45f).toDouble())
                                                val pitchDeg = (tiltAngle * 90f).coerceIn(20f, 85f)
                                                val cameraPitch = Math.toRadians(pitchDeg.toDouble())
                                                val sinYaw = kotlin.math.sin(cameraYaw).toFloat()
                                                val cosYaw = kotlin.math.cos(cameraYaw).toFloat()
                                                val sinPitch = kotlin.math.sin(cameraPitch).toFloat()
                                                val cosPitch = kotlin.math.cos(cameraPitch).toFloat()
                                                val centerX = canvasWidth / 2f
                                                val centerY = canvasHeight / 2f + 40f
                                                val globalScale = 0.85f * zoomScale
                                                val localW = tileWidth * globalScale

                                                pastDays.forEachIndexed { index, _ ->
                                                    val row = index / 7
                                                    val col = index % 7
                                                    val lx = (col - 3f) * localW
                                                    val lz = (row - 1.5f) * localW
                                                    val rx = lx * cosYaw - lz * sinYaw
                                                    val rz = lx * sinYaw + lz * cosYaw
                                                    val ry = 0f * cosPitch - rz * sinPitch
                                                    val screenX = centerX + rx
                                                    val screenY = centerY - ry

                                                    val dx = tapOffset.x - screenX
                                                    val dy = tapOffset.y - screenY
                                                    val distSq = dx * dx + dy * dy
                                                    if (distSq < minDistanceSq) {
                                                        minDistanceSq = distSq
                                                        closestIndex = index
                                                    }
                                                }
                                            } else {
                                                val startX = canvasWidth / 2f
                                                val startY = canvasHeight / 2f + 20.dp.toPx()
                                                val staticScale = 0.95f * zoomScale
                                                val tileHeight = tileWidth * 0.5f

                                                pastDays.forEachIndexed { index, _ ->
                                                    val row = index / 7
                                                    val col = index % 7
                                                    val centeredColOffset = col - 3f
                                                    val centeredRowOffset = row - 1.5f
                                                    val isoX = startX + (centeredColOffset - centeredRowOffset) * (tileWidth * staticScale * 0.48f)
                                                    val isoY = startY + (centeredColOffset + centeredRowOffset) * (tileHeight * staticScale * 0.42f)

                                                    val dx = tapOffset.x - isoX
                                                    val dy = tapOffset.y - isoY
                                                    val distSq = dx * dx + dy * dy
                                                    if (distSq < minDistanceSq) {
                                                        minDistanceSq = distSq
                                                        closestIndex = index
                                                    }
                                                }
                                            }

                                            if (closestIndex in pastDays.indices) {
                                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                                selectedDayIndex = closestIndex
                                            }
                                        }
                                    )
                                }
                        ) {
                            val tileWidth = size.width / 5f
                            val tileHeight = tileWidth * 0.5f
                            
                            if (is3DMode) {
                                // Orthographic Free Cam Camera Logic
                                val cameraYaw = Math.toRadians((rotationAngle + 45f).toDouble())
                                val pitchDeg = (tiltAngle * 90f).coerceIn(20f, 85f)
                                val cameraPitch = Math.toRadians(pitchDeg.toDouble())
                                
                                val sinYaw = kotlin.math.sin(cameraYaw).toFloat()
                                val cosYaw = kotlin.math.cos(cameraYaw).toFloat()
                                val sinPitch = kotlin.math.sin(cameraPitch).toFloat()
                                val cosPitch = kotlin.math.cos(cameraPitch).toFloat()
                                
                                val centerX = size.width / 2f
                                val centerY = size.height / 2f + 40f
                                
                                val globalScale = 0.85f * zoomScale
                                val localW = tileWidth * globalScale

                                // Convert days into 3D sorted array
                                val renderPass = pastDays.mapIndexed { index, dayDate ->
                                    val row = index / 7
                                    val col = index % 7
                                    
                                    val lx = (col - 3f) * localW
                                    val lz = (row - 1.5f) * localW
                                    
                                    val rx = lx * cosYaw - lz * sinYaw
                                    val rz = lx * sinYaw + lz * cosYaw
                                    
                                    val ry = 0f * cosPitch - rz * sinPitch
                                    val depth = 0f * sinPitch + rz * cosPitch
                                    
                                    val screenX = centerX + rx
                                    val screenY = centerY - ry
                                    
                                    Triple(index, depth, Pair(screenX, screenY))
                                }.sortedBy { it.second }

                                renderPass.forEach { (index, _, coords) ->
                                    val dayDate = pastDays[index]
                                    val screenX = coords.first
                                    val screenY = coords.second

                                    val completedTasks = state.taskActivity[dayDate] ?: 0
                                    val dayRatio = treeRatio(completedTasks, tasks.size.coerceAtLeast(1))
                                    val isSelected = index == selectedDayIndex
                                    
                                    val tileColor = when {
                                        isSelected -> primaryColor
                                        completedTasks > 0 -> Color(0xFF4ADE80)
                                        else -> Color(0xFF1E293B)
                                    }
                                    
                                    val dynamicTileHeight = localW * sinPitch

                                    drawIsoTile(screenX, screenY, localW * 1.414f, dynamicTileHeight * 1.414f, tileColor, isSelected)

                                    if (dayRatio > 0f) {
                                        drawLowPoly3DTree(screenX, screenY, globalScale * 0.9f, dayRatio, timeFloat)
                                    } else {
                                        drawLowPolyRock(screenX, screenY - 2f * globalScale, globalScale * 0.9f)
                                    }
                                }
                            } else {
                                val startX = size.width / 2f
                                val startY = size.height / 2f + 20.dp.toPx()
                                val staticScale = 0.95f * zoomScale
                                
                                pastDays.forEachIndexed { index, dayDate ->
                                    val row = index / 7
                                    val col = index % 7
                                    val completedTasks = state.taskActivity[dayDate] ?: 0
                                    val dayRatio = treeRatio(completedTasks, tasks.size.coerceAtLeast(1))

                                    val centeredColOffset = col - 3f
                                    val centeredRowOffset = row - 1.5f
                                    val isoX = startX + (centeredColOffset - centeredRowOffset) * (tileWidth * staticScale * 0.48f)
                                    val isoY = startY + (centeredColOffset + centeredRowOffset) * (tileHeight * staticScale * 0.42f)

                                    val isSelected = index == selectedDayIndex
                                    val tileColor = when {
                                        isSelected -> primaryColor
                                        completedTasks > 0 -> Color(0xFF4ADE80)
                                        else -> Color(0xFF1E293B)
                                    }

                                    drawIsoTile(isoX, isoY, tileWidth * staticScale, tileHeight * staticScale, tileColor, isSelected)

                                    if (dayRatio > 0f) {
                                        drawLowPoly3DTree(isoX, isoY, staticScale * 0.9f, dayRatio, timeFloat)
                                    } else {
                                        drawLowPolyRock(isoX, isoY - 2f * staticScale, staticScale * 0.9f)
                                    }
                                }
                            }
                        }
                    }
                    
                    // Floating Selected Date Badge on top-left of Canvas
                    val currentSelectedDate = pastDays.getOrNull(selectedDayIndex) ?: today
                    val isSelectedToday = currentSelectedDate == today
                    
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Rounded.Event,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = currentSelectedDate.format(DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.getDefault())) +
                                       (if (isSelectedToday) " (Today)" else ""),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Floating Glass Toggle
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.65f))
                            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(32.dp))
                            .padding(4.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (!is3DMode) MaterialTheme.colorScheme.primary else Color.Transparent,
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { 
                                    is3DMode = false 
                                    rotationAngle = 0f
                                    tiltAngle = 0.45f
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Rounded.GridView, 
                                    contentDescription = "2D Grid View", 
                                    modifier = Modifier.size(20.dp),
                                    tint = if (!is3DMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Surface(
                            shape = CircleShape,
                            color = if (is3DMode) MaterialTheme.colorScheme.primary else Color.Transparent,
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { is3DMode = true }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Rounded.ViewInAr, 
                                    contentDescription = "3D Orbit Camera View", 
                                    modifier = Modifier.size(20.dp),
                                    tint = if (is3DMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
                
                Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val dayListState = rememberLazyListState()
                    val coroutineScope = rememberCoroutineScope()

                    // Automatically scroll to Today on launch
                    LaunchedEffect(Unit) {
                        dayListState.scrollToItem(pastDays.lastIndex)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Garden Timeline (28 Days)",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (selectedDayIndex != pastDays.lastIndex) {
                            TextButton(
                                onClick = {
                                    selectedDayIndex = pastDays.lastIndex
                                    coroutineScope.launch {
                                        dayListState.animateScrollToItem(pastDays.lastIndex)
                                    }
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    "Jump to Today",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    LazyRow(
                        state = dayListState,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(pastDays) { index, day ->
                            val dayRatio = treeRatio(state.taskActivity[day] ?: 0, tasks.size)
                            val waterCollected = (state.taskActivity[day] ?: 0) * 25
                            GardenDayChip(
                                day = day,
                                isToday = day == today,
                                isSelected = index == selectedDayIndex,
                                dayRatio = dayRatio,
                                waterCollected = waterCollected,
                                onClick = { selectedDayIndex = index },
                                modifier = Modifier.width(58.dp)
                            )
                        }
                    }

                    // Selected Day Tree Summary & Testing Controls
                    val selectedDate = pastDays.getOrNull(selectedDayIndex) ?: today
                    val selectedActivity = state.taskActivity[selectedDate] ?: 0
                    val selectedRatio = treeRatio(selectedActivity, tasks.size)

                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .padding(end = 36.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = selectedDate.format(DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.getDefault())),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = when {
                                            selectedRatio >= 1.0f -> "100% Fully Grown Tree"
                                            selectedRatio >= 0.75f -> "75% Nearly Fully Grown"
                                            selectedRatio >= 0.50f -> "50% Half-Grown Tree"
                                            selectedRatio >= 0.25f -> "25% Small Tree"
                                            else -> "No Growth Yet (0%)"
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (selectedActivity > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                    )
                                }

                                Text(
                                    text = if (selectedActivity > 0) "Water ${selectedActivity * 25}" else "Water 0",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "$selectedActivity of ${tasks.size} completed • ${selectedActivity * 25} water collected",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Draws a 3D Isometric / Perspective Land Tile directly on Canvas.
 */
private fun DrawScope.drawIsoTile(
    isoX: Float,
    isoY: Float,
    tileWidth: Float,
    tileHeight: Float,
    tileColor: Color,
    isSelected: Boolean
) {
    val hW = tileWidth / 2f
    val hH = tileHeight / 2f
    val depth = 14f

    // Top Face
    val topPath = Path().apply {
        moveTo(isoX, isoY - hH)
        lineTo(isoX + hW, isoY)
        lineTo(isoX, isoY + hH)
        lineTo(isoX - hW, isoY)
        close()
    }
    drawPath(topPath, tileColor)
    drawPath(
        path = topPath,
        color = if (isSelected) Color.White else Color.Black.copy(alpha = 0.15f),
        style = Stroke(width = if (isSelected) 3f else 1f)
    )

    // Left Side Face
    val leftPath = Path().apply {
        moveTo(isoX - hW, isoY)
        lineTo(isoX, isoY + hH)
        lineTo(isoX, isoY + hH + depth)
        lineTo(isoX - hW, isoY + depth)
        close()
    }
    drawPath(leftPath, tileColor.copy(alpha = 0.80f))

    // Right Side Face
    val rightPath = Path().apply {
        moveTo(isoX, isoY + hH)
        lineTo(isoX + hW, isoY)
        lineTo(isoX + hW, isoY + depth)
        lineTo(isoX, isoY + hH + depth)
        close()
    }
    drawPath(rightPath, tileColor.copy(alpha = 0.60f))
}

/**
 * Draws a Low-Poly Blocky Voxel 3D Tree directly on Canvas paths based on completionRatio phases:
 * - 0%: No tree (handled caller side)
 * - 1% - 25%: Small 3D Sprout 🌱
 * - 26% - 50%: Half-grown 3D Sapling 🪴
 * - 51% - 75%: 75% Bigger 3D Tree 🌳
 * - 76% - 99%: Large 3D Tree 🌲
 * - 100%: Sacred Blooming 3D Tree 🌳🌸
 */
private fun treeRatio(completedTasks: Int, totalTasks: Int): Float =
    if (completedTasks <= 0 || totalTasks <= 0) {
        0f
    } else {
        (completedTasks.toFloat() / totalTasks.toFloat()).coerceIn(0f, 1f)
    }

private fun DrawScope.drawLowPoly3DTree(
    isoX: Float,
    isoY: Float,
    scale: Float,
    completionRatio: Float,
    time: Float = 0f
) {
    if (completionRatio <= 0f) return // 0% tasks done -> NO TREE!

    val growthStage = when {
        completionRatio >= 1.0f -> 5 // 100% All tasks done
        completionRatio >= 0.75f -> 4 // 75%-99%
        completionRatio >= 0.50f -> 3 // 50%-74%
        completionRatio >= 0.25f -> 2 // 25%-49%
        else -> 1                      // 1%-24%
    }

    val trunkW = (5f + growthStage * 1.5f) * scale
    val trunkH = (8f + growthStage * 3.5f) * scale
    val baseColor = Color(0xFF78350F) // Low-poly wood

    val trunkTopY = isoY - trunkH

    // 1. Draw 3D Wood Trunk
    val trunkLeft = Path().apply {
        moveTo(isoX - trunkW / 2, isoY)
        lineTo(isoX, isoY + trunkW / 4)
        lineTo(isoX, trunkTopY + trunkW / 4)
        lineTo(isoX - trunkW / 2, trunkTopY)
        close()
    }
    drawPath(trunkLeft, baseColor)

    val trunkRight = Path().apply {
        moveTo(isoX, isoY + trunkW / 4)
        lineTo(isoX + trunkW / 2, isoY)
        lineTo(isoX + trunkW / 2, trunkTopY)
        lineTo(isoX, trunkTopY + trunkW / 4)
        close()
    }
    drawPath(trunkRight, baseColor.copy(alpha = 0.75f))

    // 2. Draw Stacked Low-Poly 3D Foliage Cubes
    val tierCount = when (growthStage) {
        5 -> 3 // 100% Full Bloom
        4 -> 3 // 75% Large Tree
        3 -> 2 // 50% Half-grown Tree
        2 -> 1 // 25% Small Tree
        else -> 1 // <25% Sprout
    }

    var currentY = trunkTopY + 2f
    var foliageW = (16f + growthStage * 4.5f) * scale
    val foliageH = (10f + growthStage * 2.2f) * scale

    val foliageTopColors = listOf(Color(0xFF22C55E), Color(0xFF4ADE80), Color(0xFF86EFAC))
    val foliageLeftColors = listOf(Color(0xFF16A34A), Color(0xFF22C55E), Color(0xFF4ADE80))
    val foliageRightColors = listOf(Color(0xFF15803D), Color(0xFF16A34A), Color(0xFF22C55E))

    for (tier in 0 until tierCount) {
        val fW = foliageW / 2f
        val fH = foliageH / 2f

        // Top Face
        val topFoliage = Path().apply {
            moveTo(isoX, currentY - fH - foliageH)
            lineTo(isoX + fW, currentY - foliageH)
            lineTo(isoX, currentY - foliageH + fH)
            lineTo(isoX - fW, currentY - foliageH)
            close()
        }
        drawPath(topFoliage, foliageTopColors[tier % 3])

        // Left Face
        val leftFoliage = Path().apply {
            moveTo(isoX - fW, currentY - foliageH)
            lineTo(isoX, currentY - foliageH + fH)
            lineTo(isoX, currentY + fH)
            lineTo(isoX - fW, currentY)
            close()
        }
        drawPath(leftFoliage, foliageLeftColors[tier % 3])

        // Right Face
        val rightFoliage = Path().apply {
            moveTo(isoX, currentY - foliageH + fH)
            lineTo(isoX + fW, currentY - foliageH)
            lineTo(isoX + fW, currentY)
            lineTo(isoX, currentY + fH)
            close()
        }
        drawPath(rightFoliage, foliageRightColors[tier % 3])

        // Blooming Blossoms & Golden Fruits & Magic Fireflies on 100% Full Growth
        if (growthStage == 5 && tier == tierCount - 1) {
            val blossomColor = Color(0xFFF472B6)
            drawCircle(blossomColor, 4f * scale, Offset(isoX - fW * 0.45f, currentY - foliageH))
            drawCircle(blossomColor, 4f * scale, Offset(isoX + fW * 0.45f, currentY - foliageH * 0.8f))
            drawCircle(Color(0xFFFBBF24), 3.5f * scale, Offset(isoX, currentY - foliageH * 1.25f))

            // Magic Fireflies swirling around the fully grown tree
            for (i in 0 until 5) {
                val phaseOffset = i * (Math.PI / 2.5)
                val hoverX = isoX + kotlin.math.cos(time + phaseOffset).toFloat() * 32f * scale
                val hoverY = currentY - foliageH - 10f * scale + kotlin.math.sin(time * 1.5 + phaseOffset).toFloat() * 12f * scale
                
                // Outer Glow
                drawCircle(
                    color = Color.Yellow.copy(alpha = 0.3f),
                    radius = 3.5f * scale,
                    center = Offset(hoverX, hoverY)
                )
                // Inner Core
                drawCircle(
                    color = Color.White.copy(alpha = 0.9f),
                    radius = 1.2f * scale,
                    center = Offset(hoverX, hoverY)
                )
            }
        }

        currentY -= foliageH * 0.72f
        foliageW *= 0.78f
    }
}

/**
 * Draws a Low-Poly Stone / Dry Rock on Canvas for skipped days.
 */
private fun DrawScope.drawLowPolyRock(isoX: Float, isoY: Float, scale: Float = 1.0f) {
    val rockW = 10f * scale
    val rockH = 8f * scale
    val rockTop = Path().apply {
        moveTo(isoX, isoY - rockH)
        lineTo(isoX + rockW / 2, isoY - rockH / 2)
        lineTo(isoX, isoY)
        lineTo(isoX - rockW / 2, isoY - rockH / 2)
        close()
    }
    drawPath(rockTop, Color(0xFFD1D5DB))

    val rockLeft = Path().apply {
        moveTo(isoX - rockW / 2, isoY - rockH / 2)
        lineTo(isoX, isoY)
        lineTo(isoX, isoY + 3f * scale)
        lineTo(isoX - rockW / 2, isoY - rockH / 2 + 3f * scale)
        close()
    }
    drawPath(rockLeft, Color(0xFF9CA3AF))

    val rockRight = Path().apply {
        moveTo(isoX, isoY)
        lineTo(isoX + rockW / 2, isoY - rockH / 2)
        lineTo(isoX + rockW / 2, isoY - rockH / 2 + 3f * scale)
        lineTo(isoX, isoY + 3f * scale)
        close()
    }
    drawPath(rockRight, Color(0xFF6B7280))
}

@Composable
private fun MomentumCard(state: TodayUiState) {
    val progress by animateFloatAsState(
        targetValue = state.progress,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "daily_progress"
    )
    GlassSurface(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = LiquidGlassTokens.cardRadius,
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Today’s momentum",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${state.completedCount} of ${state.habits.size} complete",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                SuggestionChip(
                    onClick = { },
                    label = {
                        Text(
                            text = "${state.xp} XP",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    icon = {
                        Icon(
                            Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            ) {
                val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition()
                val wavePhase by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = (2 * Math.PI).toFloat(),
                    animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                        animation = androidx.compose.animation.core.tween(2000, easing = androidx.compose.animation.core.LinearEasing),
                        repeatMode = androidx.compose.animation.core.RepeatMode.Restart
                    ),
                    label = "liquid_wave"
                )

                val primaryColor = MaterialTheme.colorScheme.primary
                val secondaryColor = MaterialTheme.colorScheme.secondary
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val fillWidth = size.width * progress
                    if (fillWidth > 0) {
                        val path = Path().apply {
                            moveTo(0f, size.height)
                            lineTo(0f, size.height * 0.3f)
                            
                            // Draw liquid wave top edge
                            val waveCount = 3
                            val waveStep = fillWidth / 40f
                            for (i in 0..40) {
                                val x = i * waveStep
                                val yOffset = kotlin.math.sin(wavePhase + (x / fillWidth) * Math.PI * 2 * waveCount).toFloat() * 1.5f
                                lineTo(x, size.height * 0.3f + yOffset)
                            }
                            
                            lineTo(fillWidth, size.height)
                            close()
                        }
                        
                        drawPath(
                            path = path,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    primaryColor.copy(alpha = 0.8f),
                                    secondaryColor.copy(alpha = 0.9f)
                                )
                            )
                        )
                        
                        // Top Specular Highlight
                        drawRoundRect(
                            color = Color.White.copy(alpha = 0.4f),
                            topLeft = Offset(4f, 2f),
                            size = Size(fillWidth.coerceAtMost(size.width - 8f), size.height * 0.25f),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecoveryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.size(32.dp))
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "A gentle restart is available",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "Small steps still move you forward.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                )
            }
            TextButton(onClick = {}) { Text("View") }
        }
    }
}



@Composable
private fun CircularFocusDial(
    minutes: Int,
    isRunning: Boolean,
    onMinutesChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val haptics = LocalHapticFeedback.current
    var lastVibratedTenMark by remember { mutableIntStateOf((minutes / 10) * 10) }

    fun triggerTenMinVibration() {
        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
        val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? android.os.Vibrator
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            vibrator?.vibrate(android.os.VibrationEffect.createOneShot(60L, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator?.vibrate(60L)
        }
    }

    Box(
        modifier = modifier
            .size(240.dp)
            .pointerInput(isRunning) {
                if (isRunning) return@pointerInput
                detectDragGestures(
                    onDragStart = { offset ->
                        val centerX = size.width / 2f
                        val centerY = size.height / 2f
                        val dx = offset.x - centerX
                        val dy = offset.y - centerY
                        val angleRad = kotlin.math.atan2(dy, dx)
                        val angleDeg = (Math.toDegrees(angleRad.toDouble()) + 90.0 + 360.0) % 360.0
                        val rawMins = (angleDeg / 360.0 * 60.0).roundToInt().coerceIn(1, 60)
                        
                        if (rawMins != minutes) {
                            onMinutesChanged(rawMins)
                            val tenMark = (rawMins / 10) * 10
                            if (tenMark > 0 && tenMark != lastVibratedTenMark) {
                                lastVibratedTenMark = tenMark
                                triggerTenMinVibration()
                            }
                        }
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        val centerX = size.width / 2f
                        val centerY = size.height / 2f
                        val dx = change.position.x - centerX
                        val dy = change.position.y - centerY
                        val angleRad = kotlin.math.atan2(dy, dx)
                        val angleDeg = (Math.toDegrees(angleRad.toDouble()) + 90.0 + 360.0) % 360.0
                        val rawMins = (angleDeg / 360.0 * 60.0).roundToInt().coerceIn(1, 60)
                        
                        if (rawMins != minutes) {
                            onMinutesChanged(rawMins)
                            val tenMark = (rawMins / 10) * 10
                            if (tenMark > 0 && tenMark != lastVibratedTenMark) {
                                lastVibratedTenMark = tenMark
                                triggerTenMinVibration()
                            }
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        val strokePx = with(LocalDensity.current) { 14.dp.toPx() }
        val primaryColor = MaterialTheme.colorScheme.primary
        val onSurfaceColor = MaterialTheme.colorScheme.onSurface
        val textPaint = remember(onSurfaceColor) {
            android.graphics.Paint().apply {
                color = onSurfaceColor.toArgb()
                textSize = 28f
                textAlign = android.graphics.Paint.Align.CENTER
                isAntiAlias = true
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = (size.minDimension - strokePx) / 2f * 0.78f

            // 1. Background Circle Track
            drawCircle(
                color = Color.White.copy(alpha = 0.12f),
                radius = radius,
                center = center,
                style = Stroke(width = strokePx)
            )

            // 2. Active Arc
            val sweepAngle = (minutes.toFloat() / 60f) * 360f
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color(0xFF38BDF8),
                        Color(0xFF818CF8),
                        Color(0xFFF472B6),
                        Color(0xFF38BDF8)
                    )
                ),
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            // 3. 10-Minute Tick Markers & Labels (10m, 20m, 30m, 40m, 50m, 60m)
            for (m in listOf(10, 20, 30, 40, 50, 60)) {
                val tickAngleRad = Math.toRadians((-90.0 + (m / 60.0) * 360.0))
                val tickDistInner = radius - (strokePx / 2f) - 4.dp.toPx()
                val tickDistOuter = radius + (strokePx / 2f) + 4.dp.toPx()
                
                val ix = center.x + kotlin.math.cos(tickAngleRad).toFloat() * tickDistInner
                val iy = center.y + kotlin.math.sin(tickAngleRad).toFloat() * tickDistInner
                val ox = center.x + kotlin.math.cos(tickAngleRad).toFloat() * tickDistOuter
                val oy = center.y + kotlin.math.sin(tickAngleRad).toFloat() * tickDistOuter

                drawLine(
                    color = if (m <= minutes) primaryColor else Color.White.copy(alpha = 0.35f),
                    start = Offset(ix, iy),
                    end = Offset(ox, oy),
                    strokeWidth = 2.5.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Label text at outer radius
                val labelDist = radius + 20.dp.toPx()
                val lx = center.x + kotlin.math.cos(tickAngleRad).toFloat() * labelDist
                val ly = center.y + kotlin.math.sin(tickAngleRad).toFloat() * labelDist + 8f
                drawContext.canvas.nativeCanvas.drawText(
                    "${m}m",
                    lx,
                    ly,
                    textPaint.apply {
                        color = (if (m <= minutes) primaryColor else onSurfaceColor.copy(alpha = 0.6f)).toArgb()
                    }
                )
            }

            // 4. Glowing Knob Handle at Current Angle
            if (!isRunning) {
                val knobAngleRad = Math.toRadians((-90.0 + (minutes / 60.0) * 360.0))
                val kx = center.x + kotlin.math.cos(knobAngleRad).toFloat() * radius
                val ky = center.y + kotlin.math.sin(knobAngleRad).toFloat() * radius

                drawCircle(
                    color = primaryColor.copy(alpha = 0.45f),
                    radius = 14.dp.toPx(),
                    center = Offset(kx, ky)
                )
                drawCircle(
                    color = primaryColor,
                    radius = 9.dp.toPx(),
                    center = Offset(kx, ky)
                )
                drawCircle(
                    color = Color.White,
                    radius = 4.dp.toPx(),
                    center = Offset(kx, ky)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun FocusScreen(
    tasks: List<TaskEntity>,
    activeFocusTask: TaskEntity? = null,
    onClearFocusTask: () -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val nextTask = tasks
        .filterNot { it.completed }
        .minByOrNull { it.estimatedMinutes ?: Int.MAX_VALUE }

    var selectedMinutes by remember(activeFocusTask) {
        mutableIntStateOf(activeFocusTask?.estimatedMinutes?.coerceAtLeast(1) ?: 25)
    }
    var secondsLeft by remember(activeFocusTask) {
        mutableIntStateOf((activeFocusTask?.estimatedMinutes?.coerceAtLeast(1) ?: 25) * 60)
    }
    var isRunning by remember { mutableStateOf(false) }
    var completedSessions by remember { mutableIntStateOf(0) }
    var showCustomDialog by remember { mutableStateOf(false) }
    var customInputText by remember { mutableStateOf("") }
    val haptics = LocalHapticFeedback.current

    androidx.compose.runtime.LaunchedEffect(isRunning, secondsLeft) {
        FocusNotificationHelper.updateFocusNotification(context, isRunning, secondsLeft, selectedMinutes)
        if (isRunning && secondsLeft > 0) {
            // Mobile vibrates every 10 minutes during countdown (e.g. at 50m, 40m, 30m, 20m, 10m remaining)
            if (secondsLeft < (selectedMinutes * 60) && secondsLeft % (10 * 60) == 0) {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? android.os.Vibrator
                if (android.os.Build.VERSION.SDK_INT >= 26) {
                    vibrator?.vibrate(android.os.VibrationEffect.createOneShot(80L, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    vibrator?.vibrate(80L)
                }
            }
            kotlinx.coroutines.delay(1000L)
            secondsLeft--
        } else if (isRunning && secondsLeft == 0) {
            isRunning = false
            completedSessions++
            secondsLeft = selectedMinutes * 60
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    val totalSeconds = (selectedMinutes * 60).coerceAtLeast(1)
    val progress = (secondsLeft.toFloat() / totalSeconds).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "focus_progress"
    )

    val minutes = secondsLeft / 60
    val seconds = secondsLeft % 60
    val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

    AdaptiveLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = LiquidGlassTokens.contentHorizontalPadding,
            vertical = LiquidGlassTokens.pageTopSpacing
        ),
        verticalArrangement = Arrangement.spacedBy(LiquidGlassTokens.cardSpacing)
    ) {
        item {
            GlassSurface(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = LiquidGlassTokens.cardRadius,
                highlighted = true
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Pomodoro Focus Mode",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    if (activeFocusTask != null) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "🎯 Focused Task:",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        activeFocusTask.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                IconButton(onClick = onClearFocusTask) {
                                    Icon(
                                        Icons.Rounded.Close,
                                        contentDescription = "Clear focused task",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                    }

                    val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition()
                    val pulse by infiniteTransition.animateFloat(
                        initialValue = 0.95f,
                        targetValue = 1.05f,
                        animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                            animation = androidx.compose.animation.core.tween(2000, easing = androidx.compose.animation.core.FastOutSlowInEasing),
                            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                        ),
                        label = "focus_pulse"
                    )
                    
                    Box(
                        modifier = Modifier.size(240.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isRunning) {
                            Box(
                                modifier = Modifier
                                    .size(240.dp)
                                    .graphicsLayer {
                                        scaleX = pulse
                                        scaleY = pulse
                                        alpha = (1.1f - pulse) * 0.5f
                                    }
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                            )
                        }

                        // Interactive Circular Ring Dial
                        CircularFocusDial(
                            minutes = if (isRunning) (secondsLeft / 60).coerceAtLeast(1) else selectedMinutes,
                            isRunning = isRunning,
                            onMinutesChanged = { newMins ->
                                selectedMinutes = newMins
                                secondsLeft = newMins * 60
                            }
                        )

                        // Center Time Display (Clickable when paused to edit duration)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable(enabled = !isRunning) {
                                customInputText = selectedMinutes.toString()
                                showCustomDialog = true
                            }
                        ) {
                            Text(
                                text = timeFormatted,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.graphicsLayer {
                                    scaleX = if (isRunning) pulse else 1f
                                    scaleY = if (isRunning) pulse else 1f
                                }
                            )
                            Text(
                                text = if (isRunning) "Deep Focus" else "Drag ring or tap to set",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                isRunning = !isRunning
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                if (isRunning) Icons.Rounded.NotificationsActive else Icons.Rounded.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(if (isRunning) "Pause" else "Start Focus")
                        }

                        TextButton(
                            onClick = {
                                isRunning = false
                                secondsLeft = selectedMinutes * 60
                                FocusNotificationHelper.updateFocusNotification(context, false, 0, selectedMinutes)
                            }
                        ) {
                            Text("Reset", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    if (completedSessions > 0) {
                        Text(
                            text = "🎉 $completedSessions focus session${if (completedSessions > 1) "s" else ""} completed today!",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (showCustomDialog) {
            item {
                Dialog(onDismissRequest = { showCustomDialog = false }) {
                    GlassSurface(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        cornerRadius = 24.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                "Set Focus Duration (Max 60m)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            GlassInput(
                                value = customInputText,
                                onValueChange = { customInputText = it.filter { char -> char.isDigit() }.take(2) },
                                label = "Minutes (1 - 60)",
                                placeholder = "e.g. 25"
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(onClick = { showCustomDialog = false }) {
                                    Text("Cancel")
                                }
                                Spacer(Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        val mins = customInputText.toIntOrNull()?.coerceIn(1, 60) ?: 25
                                        selectedMinutes = mins
                                        secondsLeft = mins * 60
                                        isRunning = false
                                        showCustomDialog = false
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Set")
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Suggested next task",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            GlassSurface(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = LiquidGlassTokens.cardRadius
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LiquidGlassTokens.contentHorizontalPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = nextTask?.title ?: "Your task list is clear",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = nextTask?.let { "${it.estimatedMinutes ?: 0} minutes · ${it.category}" }
                                ?: "Enjoy the space or add something new.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CompletionSparkle(
    active: Boolean,
    modifier: Modifier = Modifier
) {
    val sparkProgress = remember { androidx.compose.animation.core.Animatable(0f) }

    androidx.compose.runtime.LaunchedEffect(active) {
        if (active) {
            sparkProgress.snapTo(0f)
            sparkProgress.animateTo(
                1f,
                animationSpec = androidx.compose.animation.core.tween(
                    durationMillis = 500,
                    easing = androidx.compose.animation.core.FastOutSlowInEasing
                )
            )
            sparkProgress.snapTo(0f)
        }
    }

    if (sparkProgress.value > 0f) {
        val primaryColor = MaterialTheme.colorScheme.primary
        val secondaryColor = MaterialTheme.colorScheme.secondary
        Canvas(modifier = modifier) {
            val progress = sparkProgress.value
            val alpha = (1f - progress)
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension / 2f * (0.8f + progress * 1.4f)

            // Ring shockwave
            drawCircle(
                color = primaryColor.copy(alpha = alpha * 0.5f),
                radius = radius,
                center = center,
                style = Stroke(width = 2.dp.toPx() * alpha)
            )

            // 8 Radial Sparkles
            val count = 8
            for (i in 0 until count) {
                val angle = i * (Math.PI * 2 / count)
                val dist = radius * 1.1f
                val px = center.x + kotlin.math.cos(angle).toFloat() * dist
                val py = center.y + kotlin.math.sin(angle).toFloat() * dist
                val sparkColor = if (i % 2 == 0) primaryColor else secondaryColor

                drawCircle(
                    color = sparkColor.copy(alpha = alpha),
                    radius = (3.dp.toPx() * alpha).coerceAtLeast(1f),
                    center = Offset(px, py)
                )
            }
        }
    }
}

@Composable
private fun HabitCard(
    habit: HabitEntity,
    completed: Boolean,
    onComplete: () -> Unit,
    onArchive: () -> Unit
) {
    val haptics = LocalHapticFeedback.current
    val checkScale by animateFloatAsState(
        targetValue = if (completed) 1.25f else 1.0f,
        animationSpec = spring(
            dampingRatio = 0.5f,
            stiffness = 200f
        ),
        label = "check_scale"
    )
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "habit_card_scale"
    )

    GlassSurface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { }
            ),
        cornerRadius = LiquidGlassTokens.cardRadius,
        elevation = if (completed) 0.dp else (if (isPressed) 2.dp else 4.dp),
        tint = if (completed) MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.8f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .semantics {
                    contentDescription = "${habit.name}, ${if (completed) "completed" else "not completed"}"
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        onComplete()
                    }
                )
            ) {
                CompletionSparkle(
                    active = completed,
                    modifier = Modifier.size(56.dp)
                )
                Box(modifier = Modifier.graphicsLayer {
                    scaleX = checkScale
                    scaleY = checkScale
                }) {
                    Text(habit.emoji, style = MaterialTheme.typography.headlineMedium)
                }
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .weight(1f)
            ) {
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (completed) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (completed) TextDecoration.LineThrough else TextDecoration.None
                )
                Spacer(Modifier.height(2.dp))
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            text = habit.category,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    border = null,
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            Crossfade(targetState = completed, label = "habit_completion") { isComplete ->
                if (isComplete) {
                    FilledIconButton(
                        onClick = onComplete,
                        modifier = Modifier.graphicsLayer {
                            scaleX = checkScale
                            scaleY = checkScale
                        },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(Icons.Rounded.Check, contentDescription = "Completed")
                    }
                } else {
                    IconButton(
                        onClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            onComplete()
                        },
                        modifier = Modifier.semantics {
                            contentDescription = "Complete ${habit.name}"
                        }
                    ) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            IconButton(onClick = onArchive) {
                Icon(
                    Icons.Rounded.DeleteOutline,
                    contentDescription = "Archive ${habit.name}",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun TaskSection(
    tasks: List<TaskEntity>,
    viewModel: RoutineViewModel,
    onOpenTask: () -> Unit,
    onStartFocusOnTask: ((TaskEntity) -> Unit)? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Today’s tasks",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${tasks.count { !it.completed }} remaining · ${tasks.count { it.completed }} completed",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            TextButton(onClick = onOpenTask) {
                Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text("Add task")
            }
        }
        if (tasks.isEmpty()) {
            GlassSurface(
                modifier = Modifier.fillMaxWidth().clickable(onClick = onOpenTask),
                cornerRadius = LiquidGlassTokens.cardRadius,
                elevation = 0.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "No tasks yet",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Capture the next useful step, not the whole semester.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            tasks.take(4).forEach { task ->
                TaskCard(
                    task = task,
                    onComplete = { viewModel.setTaskCompleted(task.id, !task.completed) },
                    onArchive = { viewModel.archiveTask(task.id) },
                    onStartFocus = if (onStartFocusOnTask != null) { { onStartFocusOnTask(task) } } else null
                )
            }
        }
    }
}

@Composable
private fun TaskCard(
    task: TaskEntity,
    onComplete: () -> Unit,
    onArchive: () -> Unit,
    onStartFocus: (() -> Unit)? = null
) {
    val haptics = LocalHapticFeedback.current
    
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "task_card_scale"
    )

    GlassSurface(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { }
            ),
        cornerRadius = LiquidGlassTokens.cardRadius,
        elevation = if (task.completed) 0.dp else (if (isPressed) 2.dp else 4.dp),
        tint = if (task.completed) MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.8f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val checkScale by animateFloatAsState(
                targetValue = if (task.completed) 1.25f else 1f,
                animationSpec = spring(dampingRatio = 0.5f, stiffness = 200f),
                label = "task_check_scale"
            )
            Box(
                contentAlignment = Alignment.Center
            ) {
                CompletionSparkle(
                    active = task.completed,
                    modifier = Modifier.size(48.dp)
                )
                IconButton(
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        onComplete()
                    },
                    modifier = Modifier.graphicsLayer {
                        scaleX = checkScale
                        scaleY = checkScale
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = if (task.completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (task.completed) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None
                )
                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }
            if (!task.completed && onStartFocus != null) {
                IconButton(onClick = onStartFocus) {
                    Icon(
                        Icons.Rounded.SelfImprovement,
                        contentDescription = "Start Focus Mode",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            IconButton(onClick = onArchive) {
                Icon(
                    Icons.Rounded.DeleteOutline,
                    contentDescription = "Archive task",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun AllTasksScreen(
    tasks: List<TaskEntity>,
    viewModel: RoutineViewModel,
    onOpenTask: () -> Unit,
    onStartFocusOnTask: ((TaskEntity) -> Unit)? = null
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = remember(tasks) {
        listOf("All") + tasks.map { it.category }.distinct().filter { it.isNotBlank() }
    }
    val filteredTasks = remember(tasks, selectedCategory) {
        if (selectedCategory == "All") tasks else tasks.filter { it.category == selectedCategory }
    }

    AdaptiveLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("All tasks", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text(
                        "${filteredTasks.count { !it.completed }} open · ${filteredTasks.count { it.completed }} complete",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                FilledIconButton(onClick = onOpenTask) {
                    Icon(Icons.Rounded.Add, contentDescription = "Add task")
                }
            }
        }

        if (categories.size > 1) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat) },
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }
        }

        if (filteredTasks.isEmpty()) {
            item {
                GlassSurface(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = LiquidGlassTokens.cardRadius,
                    elevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition()
                        val floatOffset by infiniteTransition.animateFloat(
                            initialValue = -4f,
                            targetValue = 4f,
                            animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                                animation = androidx.compose.animation.core.tween(1200, easing = androidx.compose.animation.core.FastOutSlowInEasing),
                                repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                            ),
                            label = "empty_icon_float"
                        )
                        Icon(
                            Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(32.dp)
                                .offset(y = floatOffset.dp)
                        )
                        Text("Your task list is clear", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            "Add the next meaningful action when you are ready.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(filteredTasks, key = { it.id }) { task ->
                Box(Modifier.animateItem()) {
                    TaskCard(
                        task = task,
                        onComplete = { viewModel.setTaskCompleted(task.id, !task.completed) },
                        onArchive = { viewModel.archiveTask(task.id) },
                        onStartFocus = if (onStartFocusOnTask != null) { { onStartFocusOnTask(task) } } else null
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlannerScreen(
    tasks: List<TaskEntity>,
    viewModel: RoutineViewModel,
    onOpenTask: () -> Unit,
    onStartFocusOnTask: ((TaskEntity) -> Unit)? = null
) {
    var subViewMode by remember { mutableIntStateOf(0) } // 0 = List View, 1 = Timeline View

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Surface(
                    onClick = { subViewMode = 0 },
                    shape = RoundedCornerShape(12.dp),
                    color = if (subViewMode == 0) MaterialTheme.colorScheme.primary else Color.Transparent,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 10.dp)
                    ) {
                        Icon(
                            Icons.Rounded.Checklist,
                            contentDescription = null,
                            tint = if (subViewMode == 0) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "List View",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (subViewMode == 0) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    onClick = { subViewMode = 1 },
                    shape = RoundedCornerShape(12.dp),
                    color = if (subViewMode == 1) MaterialTheme.colorScheme.primary else Color.Transparent,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 10.dp)
                    ) {
                        Icon(
                            Icons.Rounded.Event,
                            contentDescription = null,
                            tint = if (subViewMode == 1) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "Timeline View",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (subViewMode == 1) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        AnimatedContent(
            targetState = subViewMode,
            transitionSpec = {
                if (targetState > initialState) {
                    (slideInHorizontally { it / 3 } + fadeIn()) togetherWith (slideOutHorizontally { -it / 3 } + fadeOut())
                } else {
                    (slideInHorizontally { -it / 3 } + fadeIn()) togetherWith (slideOutHorizontally { it / 3 } + fadeOut())
                }
            },
            label = "planner_subview",
            modifier = Modifier.fillMaxSize()
        ) { view ->
            if (view == 0) {
                AllTasksScreen(
                    tasks = tasks,
                    viewModel = viewModel,
                    onOpenTask = onOpenTask,
                    onStartFocusOnTask = onStartFocusOnTask
                )
            } else {
                TaskTimelineView(
                    tasks = tasks,
                    onStartFocusOnTask = onStartFocusOnTask
                )
            }
        }
    }
}

@Composable
private fun TaskTimelineView(
    tasks: List<TaskEntity>,
    onStartFocusOnTask: ((TaskEntity) -> Unit)? = null
) {
    val today = LocalDate.now()
    val plannerTasks = remember(tasks, today) {
        tasks.filterNot { it.completed }.map { task ->
            PlannableTask(
                id = task.id,
                title = task.title,
                estimatedMinutes = task.estimatedMinutes?.coerceAtLeast(5) ?: 25,
                category = task.category,
                priority = task.priority,
                dueAt = task.dueAt?.let {
                    LocalDateTime.ofInstant(
                        java.time.Instant.ofEpochMilli(it),
                        java.time.ZoneId.systemDefault()
                    )
                },
                energy = task.energy.coerceIn(0, 2)
            )
        }
    }
    val result = remember(plannerTasks, today) {
        PlannerEngine.plan(
            PlannerRequest(
                date = today,
                workingStart = LocalTime.of(8, 30),
                workingEnd = LocalTime.of(19, 0),
                existingBlocks = listOf(
                    ExistingTimeBlock(
                        start = LocalDateTime.of(today, LocalTime.of(10, 0)),
                        end = LocalDateTime.of(today, LocalTime.of(10, 45)),
                        label = "Team Sync"
                    ),
                    ExistingTimeBlock(
                        start = LocalDateTime.of(today, LocalTime.of(14, 0)),
                        end = LocalDateTime.of(today, LocalTime.of(15, 30)),
                        label = "Focus Block"
                    )
                ),
                tasks = plannerTasks
            )
        )
    }

    AdaptiveLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text("Smart Day Planner", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(
                    "Calculates a realistic timeline around your existing calendar blocks.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        item { PlannerSummaryCard(result) }
        if (result.scheduled.isNotEmpty()) {
            item {
                Text("Suggested timeline", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            }
            items(result.scheduled, key = { it.task.id }) { block ->
                Box(Modifier.animateItem()) {
                    val matchingTask = tasks.find { it.id == block.task.id }
                    PlannerBlockCard(
                        start = block.start,
                        end = block.end,
                        title = block.task.title,
                        category = block.task.category,
                        onStartFocus = if (matchingTask != null && onStartFocusOnTask != null) { { onStartFocusOnTask(matchingTask) } } else null
                    )
                }
            }
        } else if (plannerTasks.isEmpty()) {
            item {
                GlassSurface(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = LiquidGlassTokens.cardRadius,
                    elevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition()
                        val floatOffset by infiniteTransition.animateFloat(
                            initialValue = -4f,
                            targetValue = 4f,
                            animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                                animation = androidx.compose.animation.core.tween(1200, easing = androidx.compose.animation.core.FastOutSlowInEasing),
                                repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                            ),
                            label = "empty_planner_icon_float"
                        )
                        Icon(
                            Icons.Rounded.Checklist,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(32.dp)
                                .offset(y = floatOffset.dp)
                        )
                        Text("No pending tasks", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            "Add tasks to generate a timeline.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlannerSummaryCard(result: PlannerResult) {
    val percentage = (result.workloadRatio * 100).toInt().coerceAtMost(100)
    GlassSurface(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = LiquidGlassTokens.cardRadius,
        elevation = 4.dp,
        tint = if (result.isOverloaded) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.8f)
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    if (result.isOverloaded) "Your day may be too full" else "A realistic starting point",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text("$percentage%", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            LinearProgressIndicator(
                progress = { result.workloadRatio.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = if (result.isOverloaded) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun PlannerBlockCard(
    start: LocalDateTime,
    end: LocalDateTime,
    title: String,
    category: String,
    onStartFocus: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 12.dp, top = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(2.dp, MaterialTheme.colorScheme.primaryContainer, CircleShape)
            )
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(54.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            )
                        )
                    )
            )
        }

        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        val cardScale by animateFloatAsState(
            targetValue = if (isPressed) 0.96f else 1f,
            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
            label = "planner_card_scale"
        )

        GlassSurface(
            modifier = Modifier
                .weight(1f)
                .graphicsLayer {
                    scaleX = cardScale
                    scaleY = cardScale
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = { }
                ),
            cornerRadius = LiquidGlassTokens.smallRadius,
            elevation = if (isPressed) 1.dp else 2.dp
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        category,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = "${start.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault()))} - ${end.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault()))}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
                if (onStartFocus != null) {
                    Spacer(Modifier.width(6.dp))
                    IconButton(onClick = onStartFocus) {
                        Icon(
                            Icons.Rounded.SelfImprovement,
                            contentDescription = "Start Focus Mode",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProgressScreen(state: TodayUiState) {
    val level = (state.xp / 100) + 1
    val levelProgress = (state.xp % 100) / 100f
    val totalCheckIns = state.activity.values.sum()
    val achievementCount = listOf(
        state.currentStreak >= 3,
        totalCheckIns >= 10,
        state.completedCount >= 3
    ).count { it }
    AdaptiveLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GlassSurface(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = LiquidGlassTokens.cardRadius,
                highlighted = true
            ) {
                Column(
                    modifier = Modifier.padding(LiquidGlassTokens.contentHorizontalPadding),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Level $level",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "$achievementCount achievements unlocked",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition()
                        val xpFloat by infiniteTransition.animateFloat(
                            initialValue = -3f,
                            targetValue = 3f,
                            animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                                animation = androidx.compose.animation.core.tween(1500, easing = androidx.compose.animation.core.FastOutSlowInEasing),
                                repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                            ),
                            label = "xp_float"
                        )
                        
                        Text(
                            text = "${state.xp} XP",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.offset(y = xpFloat.dp)
                        )
                    }
                    LinearProgressIndicator(
                        progress = { levelProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        strokeCap = StrokeCap.Round
                    )
                    Text(
                        text = "${100 - (state.xp % 100)} XP to the next level",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Consistency",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = when (state.currentStreak) {
                                    0 -> "Start a new streak today"
                                    1 -> "1 Day Streak 🔥"
                                    else -> "${state.currentStreak} Day Streak 🔥"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        val totalCheckIns = state.activity.values.sum()
                        Text(
                            text = if (totalCheckIns == 1) "1 check-in" else "$totalCheckIns check-ins",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    ContributionHeatmap(state.activity)
                }
            }
        }
        item {
            GlassSurface(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = LiquidGlassTokens.cardRadius
            ) {
                Column(
                    modifier = Modifier.padding(LiquidGlassTokens.contentHorizontalPadding),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Insights",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = when {
                            state.currentStreak >= 3 -> "Your consistency is building. Protect the smallest version of your routine on busy days."
                            state.completedCount > 0 -> "You have started today. One more small completion can turn momentum into a streak."
                            else -> "A single completion today is enough to create a useful signal for tomorrow."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SuggestionChip(
                            onClick = { },
                            label = { Text("Streak ${state.currentStreak}d") },
                            icon = {
                                Icon(
                                    Icons.Rounded.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                        SuggestionChip(
                            onClick = { },
                            label = { Text("$totalCheckIns check-ins") }
                        )
                    }
                }
            }
        }
        item { WeeklyGraphs(state.activity) }
    }
}

@Composable
private fun WeeklyGraphs(activity: Map<LocalDate, Int>) {
    val today = LocalDate.now()
    val days = (6 downTo 0).map { today.minusDays(it.toLong()) }
    val values = days.map { activity[it] ?: 0 }
    val maxValue = values.maxOrNull()?.coerceAtLeast(1) ?: 1
    GlassSurface(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = LiquidGlassTokens.cardRadius,
        elevation = 4.dp
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Weekly activity", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(
                        "${values.sum()} check-ins in the last 7 days",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    "Peak ${values.maxOrNull() ?: 0}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            CompletionBarChart(days, values, maxValue)
            ActivityLineChart(values, maxValue)
        }
    }
}

@Composable
private fun CompletionBarChart(days: List<LocalDate>, values: List<Int>, maxValue: Int) {
    var isVisible by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 100f),
        label = "chart_growth"
    )

    androidx.compose.runtime.LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(100)
        isVisible = true
    }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        val primaryColor = MaterialTheme.colorScheme.primary
        Canvas(
            Modifier
                .fillMaxWidth()
                .height(128.dp)
                .semantics { contentDescription = "Seven day completion bar chart" }
        ) {
            val slot = size.width / values.size
            val barWidth = slot * .52f
            values.forEachIndexed { index, value ->
                val targetHeight = size.height * value.toFloat() / maxValue
                val currentHeight = targetHeight * animatedProgress
                val left = index * slot + (slot - barWidth) / 2f
                
                // Background Track
                drawRoundRect(
                    color = primaryColor.copy(alpha = .12f),
                    topLeft = Offset(left, 0f),
                    size = Size(barWidth, size.height),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
                )

                // Foreground Fill
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.85f),
                            primaryColor.copy(alpha = 0.35f)
                        ),
                        startY = size.height - currentHeight,
                        endY = size.height
                    ),
                    topLeft = Offset(left, size.height - currentHeight),
                    size = Size(barWidth, currentHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
                )

                // Top Specular Highlight
                if (currentHeight > 10f) {
                    drawOval(
                        color = Color.White.copy(alpha = 0.5f),
                        topLeft = Offset(left + barWidth * 0.15f, size.height - currentHeight + 2f),
                        size = Size(barWidth * 0.7f, 4f)
                    )
                }
            }
        }
        
        Row(Modifier.fillMaxWidth()) {
            days.forEach { day ->
                Text(
                    day.format(DateTimeFormatter.ofPattern("EEE", Locale.getDefault())).take(1),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ActivityLineChart(values: List<Int>, maxValue: Int) {
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val surfaceColor = MaterialTheme.colorScheme.surface

    var isVisible by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = androidx.compose.animation.core.tween(1000, easing = androidx.compose.animation.core.FastOutSlowInEasing),
        label = "line_chart_reveal"
    )

    androidx.compose.runtime.LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(200)
        isVisible = true
    }

    Canvas(
        Modifier
            .fillMaxWidth()
            .height(96.dp)
            .semantics { contentDescription = "Seven day completion trend line chart" }
    ) {
        val step = if (values.size == 1) size.width else size.width / (values.size - 1)
        val points = values.mapIndexed { index, value ->
            Offset(
                x = index * step,
                y = size.height - value.toFloat() / maxValue * size.height
            )
        }
        
        // Draw background grid lines
        for (row in 1..3) {
            val y = size.height * row / 4f
            drawLine(
                color = onSurfaceColor.copy(alpha = .08f),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1.dp.toPx()
            )
        }
        
        if (points.size > 1) {
            val path = Path().apply {
                moveTo(points.first().x, points.first().y)
                points.drop(1).forEach { lineTo(it.x, it.y) }
            }
            
            // Mask the path based on the animation progress
            clipRect(right = size.width * animatedProgress) {
                // Gradient fill under the line
                val fillPath = Path().apply {
                    addPath(path)
                    lineTo(points.last().x, size.height)
                    lineTo(points.first().x, size.height)
                    close()
                }
                
                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            secondaryColor.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )

                // Draw the line itself
                drawPath(
                    path = path,
                    color = secondaryColor,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )
                
                // Draw points
                points.forEach { point ->
                    drawCircle(surfaceColor, 5.dp.toPx(), point)
                    drawCircle(secondaryColor, 3.dp.toPx(), point)
                    
                    // Specular highlight on dots
                    drawCircle(
                        color = Color.White.copy(alpha = 0.8f),
                        radius = 1.5.dp.toPx(),
                        center = Offset(point.x - 1.dp.toPx(), point.y - 1.dp.toPx())
                    )
                }
            }
        }
    }
}

@Composable
private fun ContributionHeatmap(activity: Map<LocalDate, Int>) {
    val columns = 16
    val rows = 7
    val start = LocalDate.now().minusDays((columns * rows - 1).toLong())
    val max = activity.values.maxOrNull()?.coerceAtLeast(1) ?: 1
    var selectedDateInfo by remember { mutableStateOf<Pair<LocalDate, Int>?>(null) }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val spacing = 4.dp
        val totalSpacing = spacing * (columns - 1)
        val boxWidth = ((this.maxWidth - totalSpacing) / columns).coerceAtLeast(12.dp)

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(rows) { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(columns) { column ->
                        val date = start.plusDays((column * 7L + row).coerceAtMost((columns * rows - 1).toLong()))
                        val count = activity[date] ?: 0
                        val intensity = count.toFloat() / max
                        Box(
                            modifier = Modifier
                                .size(boxWidth)
                                .clip(RoundedCornerShape(4.dp))
                                .background(heatmapColor(intensity))
                                .clickable {
                                    selectedDateInfo = date to count
                                }
                                .semantics {
                                    contentDescription = "$date: $count completions"
                                }
                        )
                    }
                }
            }

            selectedDateInfo?.let { (date, count) ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = "${date.format(DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy", Locale.getDefault()))}: $count check-in${if (count != 1) "s" else ""}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun heatmapColor(intensity: Float): Color {
    val base = MaterialTheme.colorScheme.primary
    return if (intensity <= 0f) {
        MaterialTheme.colorScheme.surfaceContainerHighest
    } else {
        base.copy(alpha = 0.3f + intensity.coerceIn(0f, 1f) * 0.7f)
    }
}

@Composable
private fun SettingsScreen(
    darkMode: Boolean,
    onThemeToggle: () -> Unit,
    reducedMotion: Boolean,
    onReducedMotionChange: (Boolean) -> Unit,
    appLockEnabled: Boolean,
    onAppLockChange: (Boolean) -> Unit
) {
    AdaptiveLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Settings", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }
        item {
            GlassSurface(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 22.dp,
                highlighted = true,
                elevation = 8.dp
            ) {
                Column {
                    ListItem(
                        headlineContent = { Text("Dark Theme") },
                        supportingContent = {
                            Text(if (darkMode) "Dark theme enabled" else "Light theme enabled")
                        },
                        leadingContent = {
                            Icon(
                                if (darkMode) Icons.Rounded.DarkMode else Icons.Rounded.LightMode,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingContent = {
                            Switch(
                                checked = darkMode,
                                onCheckedChange = { onThemeToggle() }
                            )
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
            }
        }
        item {
            GlassSurface(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 22.dp,
                highlighted = true,
                elevation = 8.dp
            ) {
                Column {
                    ListItem(
                        headlineContent = { Text("Gentle Reminders") },
                        supportingContent = { Text("Daily inspiring quote notifications") },
                        leadingContent = {
                            Icon(
                                Icons.Rounded.NotificationsActive,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                    ListItem(
                        headlineContent = { Text("Sacred Tree Garden") },
                        supportingContent = { Text("2.5D & 3D Interactive Forest Gaming Mode") },
                        leadingContent = {
                            Icon(
                                Icons.Rounded.Palette,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
            }
        }
        item {
            GlassSurface(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 22.dp,
                highlighted = true,
                elevation = 8.dp
            ) {
                Column {
                    ListItem(
                        headlineContent = { Text("App Lock") },
                        supportingContent = {
                            Text(if (appLockEnabled) "Biometric authentication required to open app" else "App lock disabled")
                        },
                        leadingContent = {
                            Icon(
                                Icons.Rounded.Settings,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingContent = {
                            Switch(
                                checked = appLockEnabled,
                                onCheckedChange = onAppLockChange
                            )
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
            }
        }
        item {
            GlassSurface(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 22.dp,
                highlighted = true,
                elevation = 8.dp
            ) {
                ListItem(
                    headlineContent = { Text("Reduce Motion") },
                    supportingContent = {
                        Text(
                            if (reducedMotion) {
                                "Animations are minimized"
                            } else {
                                "Use spring motion and screen transitions"
                            }
                        )
                    },
                    leadingContent = {
                        Icon(
                            Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = reducedMotion,
                            onCheckedChange = onReducedMotionChange
                        )
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
            }
        }
    }
}

data class PresetHabit(
    val name: String,
    val category: String,
    val emoji: String,
    val reminderMinutes: Int? = null
)

private val PRESET_HABITS = listOf(
    PresetHabit("Drink Water", "Health", "💧", 8 * 60),
    PresetHabit("10m Meditation", "Mindful", "🧘", 7 * 60 + 30),
    PresetHabit("Read 20 Mins", "Focus", "📖", 21 * 60),
    PresetHabit("Morning Walk", "Fitness", "🏃", 7 * 60),
    PresetHabit("Deep Work", "Focus", "💻", 9 * 60)
)

private val CATEGORY_PRESETS = listOf(
    Pair("Wellness", "🧘"),
    Pair("Focus", "📚"),
    Pair("Fitness", "🏃"),
    Pair("Mindful", "🌿"),
    Pair("Health", "💧")
)

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun AddHabitDialog(
    reducedMotion: Boolean,
    onDismiss: () -> Unit,
    onAdd: (String, String, String, Int?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Wellness") }
    var emoji by remember { mutableStateOf("✨") }
    var reminder by remember { mutableStateOf("") }
    val entryProgress = remember { androidx.compose.animation.core.Animatable(0f) }
    val revealAccent = MaterialTheme.colorScheme.primary

    androidx.compose.runtime.LaunchedEffect(Unit) {
        if (reducedMotion) {
            entryProgress.snapTo(1f)
        } else {
            entryProgress.animateTo(
                1f,
                animationSpec = spring(
                    dampingRatio = 0.82f,
                    stiffness = 500f
                )
            )
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable { onDismiss() }
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 92.dp)
                    .graphicsLayer {
                        val p = entryProgress.value
                        alpha = p.coerceIn(0f, 1f)
                        translationY = androidx.compose.ui.util.lerp(32f, 0f, p)
                        val s = androidx.compose.ui.util.lerp(0.95f, 1f, p)
                        scaleX = s
                        scaleY = s
                    }
                    .clip(RoundedCornerShape(LiquidGlassTokens.cardRadius))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable(enabled = false) {}
            ) {
                GlassSurface(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = LiquidGlassTokens.cardRadius,
                    elevation = LiquidGlassTokens.floatingElevation,
                    tint = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Text(
                            text = "Add New Habit",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.liquidMenuReveal(entryProgress.value, 0.08f, 0.42f, reducedMotion)
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.liquidMenuReveal(entryProgress.value, 0.16f, 0.5f, reducedMotion)
                        ) {
                            Text(
                                text = "Quick templates",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                PRESET_HABITS.forEach { preset ->
                                    SuggestionChip(
                                        onClick = {
                                            onAdd(preset.name, preset.category, preset.emoji, preset.reminderMinutes)
                                        },
                                        label = { Text("${preset.emoji} ${preset.name}") },
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                }
                            }
                        }

                        GlassInput(
                            value = name,
                            onValueChange = { name = it },
                            label = "Habit name",
                            placeholder = "e.g. Read 15 pages",
                            modifier = Modifier.liquidMenuReveal(entryProgress.value, 0.25f, 0.6f, reducedMotion)
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.liquidMenuReveal(entryProgress.value, 0.34f, 0.7f, reducedMotion)
                        ) {
                            Text(
                                text = "Category & Icon",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CATEGORY_PRESETS.forEach { (catName, catEmoji) ->
                                    val selected = category == catName
                                    FilterChip(
                                        selected = selected,
                                        onClick = {
                                            category = catName
                                            emoji = catEmoji
                                        },
                                        label = { Text("$catEmoji $catName") },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    )
                                }
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.liquidMenuReveal(entryProgress.value, 0.42f, 0.8f, reducedMotion)
                        ) {
                            GlassInput(
                                value = emoji,
                                onValueChange = { emoji = it.take(2) },
                                label = "Emoji",
                                modifier = Modifier.weight(0.3f)
                            )
                            GlassInput(
                                value = category,
                                onValueChange = { category = it },
                                label = "Category",
                                modifier = Modifier.weight(0.7f)
                            )
                        }

                        GlassInput(
                            value = reminder,
                            onValueChange = { reminder = it.take(5) },
                            label = "Daily reminder (HH:mm, optional)",
                            placeholder = "08:30",
                            modifier = Modifier.liquidMenuReveal(entryProgress.value, 0.48f, 0.9f, reducedMotion)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .liquidMenuReveal(entryProgress.value, 0.54f, 0.98f, reducedMotion),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = onDismiss,
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Cancel", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(
                                onClick = {
                                    val parts = reminder.split(":")
                                    val hour = parts.getOrNull(0)?.toIntOrNull()
                                    val minute = parts.getOrNull(1)?.toIntOrNull()
                                    val minutes = if (
                                        parts.size == 2 &&
                                        hour != null && hour in 0..23 &&
                                        minute != null && minute in 0..59
                                    ) {
                                        hour * 60 + minute
                                    } else {
                                        null
                                    }
                                    onAdd(name, category, emoji, minutes)
                                },
                                enabled = name.isNotBlank() && (
                                    reminder.isBlank() ||
                                        reminder.matches(Regex("([01]\\d|2[0-3]):[0-5]\\d"))
                                    ),
                                shape = RoundedCornerShape(16.dp),
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Text("Add habit", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddTaskDialog(
    reducedMotion: Boolean,
    onDismiss: () -> Unit,
    onAdd: (String, String, String, Int, Int?, Long?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Personal") }
    var estimate by remember { mutableStateOf("25") }
    val entryProgress = remember { androidx.compose.animation.core.Animatable(0f) }
    val revealAccent = MaterialTheme.colorScheme.primary

    androidx.compose.runtime.LaunchedEffect(Unit) {
        if (reducedMotion) {
            entryProgress.snapTo(1f)
        } else {
            entryProgress.animateTo(
                1f,
                animationSpec = spring(
                    dampingRatio = 0.82f,
                    stiffness = 500f
                )
            )
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable { onDismiss() }
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 92.dp)
                    .graphicsLayer {
                        val p = entryProgress.value
                        alpha = p.coerceIn(0f, 1f)
                        translationY = androidx.compose.ui.util.lerp(32f, 0f, p)
                        val s = androidx.compose.ui.util.lerp(0.95f, 1f, p)
                        scaleX = s
                        scaleY = s
                    }
                    .clip(RoundedCornerShape(LiquidGlassTokens.cardRadius))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable(enabled = false) {}
            ) {
                GlassSurface(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = LiquidGlassTokens.cardRadius,
                    elevation = LiquidGlassTokens.floatingElevation,
                    tint = MaterialTheme.colorScheme.surface
                ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "Add New Task",
                        modifier = Modifier.liquidMenuReveal(entryProgress.value, 0.08f, 0.42f, reducedMotion),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    GlassInput(
                        value = title,
                        onValueChange = { title = it },
                        label = "Task Title",
                        modifier = Modifier.liquidMenuReveal(entryProgress.value, 0.16f, 0.5f, reducedMotion)
                    )

                    GlassInput(
                        value = description,
                        onValueChange = { description = it },
                        label = "Description (Optional)",
                        modifier = Modifier.liquidMenuReveal(entryProgress.value, 0.25f, 0.6f, reducedMotion)
                    )

                    Row(
                        modifier = Modifier.liquidMenuReveal(entryProgress.value, 0.34f, 0.7f, reducedMotion),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        GlassInput(
                            value = category,
                            onValueChange = { category = it },
                            label = "Category",
                            modifier = Modifier.weight(1f)
                        )
                        GlassInput(
                            value = estimate,
                            onValueChange = { estimate = it.filter { char -> char.isDigit() }.take(3) },
                            label = "Minutes",
                            modifier = Modifier.weight(0.5f)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .liquidMenuReveal(entryProgress.value, 0.48f, 0.92f, reducedMotion),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Cancel", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(
                            onClick = {
                                onAdd(
                                    title,
                                    description,
                                    category,
                                    1,
                                    estimate.toIntOrNull() ?: 25,
                                    null
                                )
                            },
                            enabled = title.isNotBlank(),
                            shape = RoundedCornerShape(16.dp),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text("Add task", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                }
            }
        }
    }
}

private fun Modifier.liquidMenuReveal(
    progress: Float,
    start: Float,
    end: Float,
    reducedMotion: Boolean
): Modifier {
    if (reducedMotion) return this
    val normalized = ((progress - start) / (end - start)).coerceIn(0f, 1f)
    if (normalized <= 0f) {
        return graphicsLayer { alpha = 0f }
    }

    val eased = androidx.compose.animation.core.FastOutSlowInEasing.transform(normalized)

    return graphicsLayer {
        alpha = eased
        translationY = (1f - eased) * 10f
    }
}

@Composable
private fun GlassInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    singleLine: Boolean = true
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusScale by animateFloatAsState(
        targetValue = if (isFocused) 1.015f else 1f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 400f),
        label = "inputFocusScale"
    )

    androidx.compose.material3.OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontWeight = FontWeight.Medium) },
        placeholder = { if (placeholder.isNotBlank()) Text(placeholder) },
        singleLine = singleLine,
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = focusScale
                scaleY = focusScale
            }
            .onFocusChanged { isFocused = it.isFocused },
        shape = RoundedCornerShape(16.dp),
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.55f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.22f),
            focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    )
}
