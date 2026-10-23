package com.routineflow

import android.os.Bundle
import androidx.activity.ComponentActivity
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
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.LocalFlorist
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.routineflow.data.local.HabitEntity
import com.routineflow.data.local.TaskEntity
import com.routineflow.domain.ExistingTimeBlock
import com.routineflow.domain.PlannableTask
import com.routineflow.domain.PlannerEngine
import com.routineflow.domain.PlannerRequest
import com.routineflow.domain.PlannerResult
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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
    var darkMode by remember { mutableStateOf(systemDarkMode) }
    val onboardingViewModel: OnboardingViewModel = hiltViewModel()
    val onboarding by onboardingViewModel.state.collectAsStateWithLifecycle()
    val reducedMotion by onboardingViewModel.reducedMotion.collectAsStateWithLifecycle()
    RoutineFlowTheme(darkTheme = darkMode) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (onboarding.completed) {
                RoutineFlowApp(
                    darkMode = darkMode,
                    onThemeToggle = { darkMode = !darkMode },
                    reducedMotion = reducedMotion,
                    onReducedMotionChange = onboardingViewModel::setReducedMotion
                )
            } else {
                OnboardingScreen(onFinished = {})
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RoutineFlowApp(
    darkMode: Boolean,
    onThemeToggle: () -> Unit,
    reducedMotion: Boolean,
    onReducedMotionChange: (Boolean) -> Unit,
    viewModel: RoutineViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    var showAdd by remember { mutableStateOf(false) }
    var showAddTask by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    val haptics = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (selectedTab) {
                            0 -> "RoutineFlow"
                            1 -> "Sacred Garden 🌱"
                            2 -> "Progress"
                            3 -> "Tasks"
                            4 -> "Planner"
                            else -> "Settings"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = selectedTab == 0,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                var addPressed by remember { mutableStateOf(false) }
                val addScale by animateFloatAsState(
                    targetValue = if (addPressed) 0.9f else 1f,
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
                                center = androidx.compose.ui.geometry.Offset(
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
                        .pointerInput(reducedMotion) {
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
                                    showAddTask = true
                                }
                            )
                        }
                        .semantics {
                            role = Role.Button
                            contentDescription = "Add a new habit"
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
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(28.dp)
                            .graphicsLayer {
                                scaleX = if (addPressed) 0.88f else 1f
                                scaleY = if (addPressed) 0.88f else 1f
                                rotationZ = if (addPressed) -8f else 0f
                            }
                    )
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
                        fadeIn() togetherWith fadeOut()
                    } else if (targetState > initialState) {
                        (slideInHorizontally { it / 5 } + fadeIn()) togetherWith
                            (slideOutHorizontally { -it / 5 } + fadeOut())
                    } else {
                        (slideInHorizontally { -it / 5 } + fadeIn()) togetherWith
                            (slideOutHorizontally { it / 5 } + fadeOut())
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
                        onOpenTask = { showAddTask = true }
                    )
                    1 -> GardenScreen(state)
                    2 -> ProgressScreen(state)
                    3 -> AllTasksScreen(tasks, viewModel, onOpenTask = { showAddTask = true })
                    4 -> PlannerScreen(tasks)
                    else -> SettingsScreen(
                        darkMode = darkMode,
                        onThemeToggle = onThemeToggle,
                        reducedMotion = reducedMotion,
                        onReducedMotionChange = onReducedMotionChange
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
            onDismiss = { showAdd = false },
            onAdd = { name, category, emoji, reminderMinutes ->
                viewModel.addHabit(name, category, emoji, reminderMinutes)
                showAdd = false
            }
        )
    }
    if (showAddTask) {
        AddTaskDialog(
            onDismiss = { showAddTask = false },
            onAdd = { title, description, category, priority, estimate, dueAt ->
                viewModel.addTask(title, description, category, priority, dueAt, estimate)
                showAddTask = false
            }
        )
    }
}

@Composable
private fun RoutineFlowDock(
    selectedTab: Int,
    reducedMotion: Boolean,
    onTabSelected: (Int) -> Unit
) {
    val labels = listOf("Today", "Garden", "Progress", "Tasks", "Planner", "Settings")
    val icons = listOf(
        Icons.Rounded.Check,
        Icons.Rounded.LocalFlorist,
        Icons.Rounded.Insights,
        Icons.Rounded.Check,
        Icons.Rounded.AutoAwesome,
        Icons.Rounded.Settings
    )
    val density = LocalDensity.current
    val haptics = LocalHapticFeedback.current
    val isDarkMode = isSystemInDarkTheme()

    var widthPx by remember { mutableFloatStateOf(0f) }
    var dragging by remember { mutableStateOf(false) }
    val slotPx = if (labels.isNotEmpty()) widthPx / labels.size else 0f

    // Use Animatable for continuous physics-based tracking, mimicking ClearPDF's DampedDragAnimation
    val indicatorIndexAnim = remember { androidx.compose.animation.core.Animatable(selectedTab.toFloat()) }
    val panelOffsetAnim = remember { androidx.compose.animation.core.Animatable(0f) }
    val pressProgressAnim = remember { androidx.compose.animation.core.Animatable(0f) }

    // Sync external tab changes
    androidx.compose.runtime.LaunchedEffect(selectedTab) {
        if (!dragging) {
            indicatorIndexAnim.animateTo(
                targetValue = selectedTab.toFloat(),
                animationSpec = if (reducedMotion) androidx.compose.animation.core.snap() else spring(stiffness = Spring.StiffnessMediumLow)
            )
        }
    }

    fun tabFor(x: Float): Int {
        if (slotPx <= 0f) return selectedTab
        return (x / slotPx).toInt().coerceIn(labels.indices)
    }

    val dockScope = androidx.compose.runtime.rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .height(72.dp)
            .graphicsLayer {
                val scale = androidx.compose.ui.util.lerp(1f, 1.04f, pressProgressAnim.value)
                scaleX = scale
                scaleY = scale
                translationX = panelOffsetAnim.value
            },
        contentAlignment = Alignment.Center
    ) {
        // Liquid Glass Container Layer
        GlassSurface(
            modifier = Modifier.fillMaxSize(),
            cornerRadius = 36.dp, // Fully rounded capsule
            elevation = 12.dp,
            highlighted = true,
            tint = if (isDarkMode) Color(0xFF1E1E1E).copy(alpha = 0.5f) else Color(0xFFF9F9F9).copy(alpha = 0.6f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged { widthPx = it.width.toFloat() }
                    .pointerInput(slotPx, reducedMotion) {
                        awaitEachGesture {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            dragging = true
                            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)

                            // Launch press animation
                            dockScope.launch {
                                pressProgressAnim.animateTo(1f, spring(stiffness = Spring.StiffnessMedium))
                            }

                            var currentX = down.position.x
                            dockScope.launch {
                                indicatorIndexAnim.snapTo(currentX / slotPx)
                            }

                            var released = false
                            while (!released) {
                                val event = awaitPointerEvent()
                                event.changes.forEach { change ->
                                    if (change.pressed) {
                                        currentX = change.position.x
                                        val rawIndex = currentX / slotPx
                                        
                                        // Handle drag bounds and elastic panel offset
                                        val maxIndex = labels.size - 1f
                                        val overscroll = when {
                                            rawIndex < 0f -> rawIndex
                                            rawIndex > maxIndex -> rawIndex - maxIndex
                                            else -> 0f
                                        }

                                        dockScope.launch {
                                            indicatorIndexAnim.snapTo(rawIndex.coerceIn(0f, maxIndex))
                                            
                                            // Apply ClearPDF style dampening/elasticity to the panel
                                            val maxOverscrollPx = 40f
                                            val pullFraction = (overscroll * slotPx / widthPx).coerceIn(-1f, 1f)
                                            val elasticOffset = pullFraction * maxOverscrollPx
                                            panelOffsetAnim.snapTo(elasticOffset)
                                        }
                                    } else {
                                        released = true
                                    }
                                }
                            }

                            dragging = false
                            val finalTab = tabFor(currentX)
                            onTabSelected(finalTab)

                            // Release animations
                            dockScope.launch {
                                launch { pressProgressAnim.animateTo(0f, spring(stiffness = Spring.StiffnessMediumLow)) }
                                launch { panelOffsetAnim.animateTo(0f, spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium)) }
                                launch { indicatorIndexAnim.animateTo(finalTab.toFloat(), spring(stiffness = Spring.StiffnessMediumLow)) }
                            }
                        }
                    }
            ) {
                // Interactive Highlight / Active Pill
                if (slotPx > 0f) {
                    val indicatorCenter = slotPx * (indicatorIndexAnim.value + 0.5f)
                    val pillWidth = slotPx * 0.85f

                    // Squeeze effect based on drag velocity/movement
                    val isSqueezed = dragging
                    val animatedScaleX by animateFloatAsState(if (isSqueezed) 1.15f else 1f, spring(stiffness = Spring.StiffnessMedium))
                    val animatedScaleY by animateFloatAsState(if (isSqueezed) 0.85f else 1f, spring(stiffness = Spring.StiffnessMedium))

                    Box(
                        modifier = Modifier
                            .width(with(density) { pillWidth.toDp() })
                            .fillMaxHeight()
                            .graphicsLayer {
                                translationX = indicatorCenter - (pillWidth / 2f)
                                scaleX = animatedScaleX
                                scaleY = animatedScaleY
                            }
                            .padding(vertical = 8.dp)
                            .clip(RoundedCornerShape(36.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                    )
                                )
                            )
                            .border(
                                1.dp,
                                Brush.verticalGradient(
                                    0f to Color.White.copy(alpha = 0.4f),
                                    1f to Color.White.copy(alpha = 0.05f)
                                ),
                                RoundedCornerShape(36.dp)
                            )
                    )
                }

                Row(Modifier.fillMaxSize()) {
                    labels.forEachIndexed { index, label ->
                        val targetIndex = if (dragging) indicatorIndexAnim.value.toInt().coerceIn(labels.indices) else selectedTab
                        val highlighted = index == targetIndex

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .semantics {
                                    contentDescription = "$label tab${if (index == selectedTab) ", selected" else ""}"
                                }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                Icon(
                                    imageVector = icons[index],
                                    contentDescription = null,
                                    tint = if (highlighted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    fontWeight = if (highlighted) FontWeight.Bold else FontWeight.Medium,
                                    color = if (highlighted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RoutineFlowDockClearPdf(
    selectedTab: Int,
    reducedMotion: Boolean,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val labels = listOf("Today", "Garden", "Progress", "Tasks", "Planner", "Settings")
    val icons = listOf(
        Icons.Rounded.Check,
        Icons.Rounded.LocalFlorist,
        Icons.Rounded.Insights,
        Icons.Rounded.Check,
        Icons.Rounded.AutoAwesome,
        Icons.Rounded.Settings
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
                    elevation = 10.dp,
                    shape = CircleShape,
                    ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                    spotColor = Color.Black.copy(alpha = 0.12f)
                )
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.65f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
                            Color.White.copy(alpha = 0.45f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        0f to Color.White.copy(alpha = 0.85f),
                        0.28f to Color.White.copy(alpha = 0.45f),
                        1f to MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
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
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                )
                            )
                        )
                        .border(
                            1.dp,
                            Brush.verticalGradient(
                                0f to Color.White.copy(alpha = 0.75f),
                                1f to Color.Transparent
                            ),
                            CircleShape
                        )
                )
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
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val contentWidth = when {
            maxWidth >= 1000.dp -> LiquidGlassTokens.wideContentWidth
            maxWidth >= 600.dp -> LiquidGlassTokens.compactContentWidth
            else -> maxWidth
        }
        LazyColumn(
            modifier = Modifier
                .width(contentWidth)
                .align(Alignment.TopCenter),
            contentPadding = if (maxWidth >= 600.dp) {
                PaddingValues(
                    start = contentPadding.calculateLeftPadding(androidx.compose.ui.unit.LayoutDirection.Ltr) + 8.dp,
                    end = contentPadding.calculateRightPadding(androidx.compose.ui.unit.LayoutDirection.Ltr) + 8.dp,
                    top = contentPadding.calculateTopPadding(),
                    bottom = contentPadding.calculateBottomPadding()
                )
            } else {
                contentPadding
            },
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
    onOpenTask: () -> Unit
) {
    AdaptiveLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Good evening",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = LocalDate.now().format(
                        DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.getDefault())
                    ),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        item { DailyTreeCard(state, tasks) }
        item { MomentumCard(state) }
        item { TaskSection(tasks, viewModel, onOpenTask) }
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
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(48.dp)
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
            HabitCard(
                habit = habit,
                completed = habit.id in state.completedIds,
                onComplete = { viewModel.complete(habit.id) },
                onArchive = { viewModel.archive(habit.id) }
            )
        }
    }
}

/**
 * Interactive Daily Tree Card on the Today Screen.
 * Renders a low-poly 3D tree canvas that grows as tasks/habits are completed!
 */
@Composable
private fun DailyTreeCard(state: TodayUiState, tasks: List<TaskEntity>) {
    val haptics = LocalHapticFeedback.current
    var extraWater by remember { mutableIntStateOf(0) }
    var isWatering by remember { mutableStateOf(false) }

    val completedHabits = state.completedCount
    val completedTasks = tasks.count { it.completed }
    val totalItems = (state.habits.size + tasks.size).coerceAtLeast(1)
    val completedItems = completedHabits + completedTasks
    val waterDrops = (completedHabits * 25) + (completedTasks * 20) + extraWater
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
                        text = "Today's Sacred Tree 🌱",
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
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Rounded.WaterDrop,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "💧 $waterDrops Drops",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
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
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = if (isWatering) 1.15f else 1.0f
                            scaleY = if (isWatering) 1.15f else 1.0f
                        }
                ) {
                    val centerX = size.width / 2f
                    val centerY = size.height * 0.82f

                    drawIsoTile(centerX, centerY, 140f, 75f, Color(0xFF4ADE80), false)
                    if (animatedGrowth > 0f) {
                        drawLowPoly3DTree(centerX, centerY, 1.4f, animatedGrowth)
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

                Spacer(Modifier.width(12.dp))

                FilledTonalButton(
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        extraWater += 50
                        isWatering = true
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Rounded.WaterDrop,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Test Water 💧")
                }
            }
        }
    }
}

/**
 * Dedicated Interactive Gaming Section (Sacred Tree Garden).
 * Features Low-Poly 3D Trees drawn directly on Canvas tiles for 2.5D Isometric & 3D Perspective!
 */
@Composable
private fun GardenScreen(state: TodayUiState) {
    var is3DMode by remember { mutableStateOf(false) }
    var tiltAngle by remember { mutableFloatStateOf(0.45f) }
    var selectedDayIndex by remember { mutableIntStateOf(0) }
    var testWaterBoost by remember { mutableIntStateOf(0) }

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
                        Column {
                            Text(
                                "Sacred Forest Garden 🌱",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Trees grow based on task completion phases (25%, 50%, 75%, 100%).",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        AssistChip(
                            onClick = {},
                            label = { Text("${pastDays.count { (state.activity[it] ?: 0) > 0 }} Trees Grown") },
                            leadingIcon = { Icon(Icons.Rounded.LocalFlorist, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        )
                    }

                    // View Mode Switcher (2.5D Isometric vs 3D Perspective)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (!is3DMode) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { is3DMode = false }
                        ) {
                            Box(Modifier.padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    "2.5D Isometric Grid",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (!is3DMode) FontWeight.Bold else FontWeight.Medium,
                                    color = if (!is3DMode) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (is3DMode) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { is3DMode = true }
                        ) {
                            Box(Modifier.padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    "3D Perspective Forest",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (is3DMode) FontWeight.Bold else FontWeight.Medium,
                                    color = if (is3DMode) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        if (is3DMode) {
            item {
                OutlinedCard(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    Row(
                        Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("3D Camera Angle:", style = MaterialTheme.typography.labelMedium)
                        Spacer(Modifier.width(12.dp))
                        Slider(
                            value = tiltAngle,
                            onValueChange = { tiltAngle = it },
                            valueRange = 0.2f..0.8f,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Low-Poly 3D Garden Canvas
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    val primaryColor = MaterialTheme.colorScheme.primary

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(340.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    ) {
                        val cols = 7
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(is3DMode, tiltAngle) {
                                    detectTapGestures { offset ->
                                        val startX = size.width / 2f
                                        val startY = if (is3DMode) size.height * (1f - tiltAngle) else 80.dp.toPx()
                                        val tileWidth = size.width / 5.2f
                                        val tileHeight = tileWidth * 0.52f

                                        val closestIndex = pastDays.indices.minByOrNull { index ->
                                            val row = index / cols
                                            val col = index % cols
                                            val isoX = if (!is3DMode) {
                                                startX + (col - row) * (tileWidth * 0.48f)
                                            } else {
                                                val depth = 1.0f + row * 0.35f
                                                val scale = 1.0f / depth
                                                startX + (col - cols / 2f) * (tileWidth * scale * 0.9f)
                                            }
                                            val isoY = if (!is3DMode) {
                                                startY + (col + row) * (tileHeight * 0.42f)
                                            } else {
                                                startY + row * (tileHeight * 0.85f * (1.2f - tiltAngle))
                                            }
                                            val dx = offset.x - isoX
                                            val dy = offset.y - isoY
                                            dx * dx + dy * dy
                                        } ?: 0
                                        selectedDayIndex = closestIndex
                                    }
                                }
                        ) {
                            val tileWidth = size.width / 5.2f
                            val tileHeight = tileWidth * 0.52f
                            val startX = size.width / 2f
                            val startY = if (is3DMode) size.height * (1f - tiltAngle) else 80.dp.toPx()

                            pastDays.forEachIndexed { index, dayDate ->
                                val row = index / cols
                                val col = index % cols
                                val activityCount = state.activity[dayDate] ?: 0
                                val dayRatio = (activityCount / 4f).coerceIn(0f, 1f)

                                val isoX = if (!is3DMode) {
                                    startX + (col - row) * (tileWidth * 0.48f)
                                } else {
                                    val depth = 1.0f + row * 0.35f
                                    val scale = 1.0f / depth
                                    startX + (col - cols / 2f) * (tileWidth * scale * 0.9f)
                                }

                                val isoY = if (!is3DMode) {
                                    startY + (col + row) * (tileHeight * 0.42f)
                                } else {
                                    startY + row * (tileHeight * 0.85f * (1.2f - tiltAngle))
                                }

                                val isSelected = index == selectedDayIndex
                                val tileColor = when {
                                    isSelected -> primaryColor
                                    activityCount > 0 -> Color(0xFF4ADE80) // Lush green grass
                                    else -> Color(0xFFE5E7EB) // Dry earth skipped tile
                                }

                                // 1. Draw 3D Isometric Land Tile
                                drawIsoTile(isoX, isoY, tileWidth, tileHeight, tileColor, isSelected)

                                // 2. Draw Low-Poly Voxel 3D Tree according to exact phase or Stone for 0%
                                if (dayRatio > 0f) {
                                    val scale = if (is3DMode) 1.0f / (1.0f + row * 0.25f) else 1.0f
                                    drawLowPoly3DTree(isoX, isoY, scale, dayRatio)
                                } else {
                                    drawLowPolyRock(isoX, isoY)
                                }
                            }
                        }
                    }

                    // Selected Day Tree Summary & Testing Controls
                    val selectedDate = pastDays.getOrNull(selectedDayIndex) ?: today
                    val selectedActivity = (state.activity[selectedDate] ?: 0) + testWaterBoost
                    val selectedRatio = (selectedActivity / 4f).coerceIn(0f, 1f)

                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
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
                                            selectedRatio >= 1.0f -> "100% Fully Grown & Blooming! 🌳🌸"
                                            selectedRatio >= 0.75f -> "75% Large Tree Phase 🌲"
                                            selectedRatio >= 0.50f -> "50% Half-grown Tree 🪴"
                                            selectedRatio >= 0.25f -> "25% Small Sprout Phase 🌿"
                                            else -> "0% Skipped Day (No tree planted) 🪨"
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (selectedActivity > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                    )
                                }

                                Text(
                                    text = if (selectedActivity > 0) "💧 ${selectedActivity * 25} Drops" else "0 Drops",
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
                                Text("Test Growth on Selected Tile:", style = MaterialTheme.typography.labelMedium)
                                FilledTonalButton(
                                    onClick = { testWaterBoost = (testWaterBoost + 1) % 5 },
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("+1 Growth Stage 🌱")
                                }
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
private fun DrawScope.drawLowPoly3DTree(
    isoX: Float,
    isoY: Float,
    scale: Float,
    completionRatio: Float
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

        // Blooming Blossoms & Golden Fruits on 100% Full Growth
        if (growthStage == 5 && tier == tierCount - 1) {
            val blossomColor = Color(0xFFF472B6)
            drawCircle(blossomColor, 4f * scale, Offset(isoX - fW * 0.45f, currentY - foliageH))
            drawCircle(blossomColor, 4f * scale, Offset(isoX + fW * 0.45f, currentY - foliageH * 0.8f))
            drawCircle(Color(0xFFFBBF24), 3.5f * scale, Offset(isoX, currentY - foliageH * 1.25f))
        }

        currentY -= foliageH * 0.72f
        foliageW *= 0.78f
    }
}

/**
 * Draws a Low-Poly Stone / Dry Rock on Canvas for skipped days.
 */
private fun DrawScope.drawLowPolyRock(isoX: Float, isoY: Float) {
    val rockW = 10f
    val rockH = 8f
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
        lineTo(isoX, isoY + 3f)
        lineTo(isoX - rockW / 2, isoY - rockH / 2 + 3f)
        close()
    }
    drawPath(rockLeft, Color(0xFF9CA3AF))

    val rockRight = Path().apply {
        moveTo(isoX, isoY)
        lineTo(isoX + rockW / 2, isoY - rockH / 2)
        lineTo(isoX + rockW / 2, isoY - rockH / 2 + 3f)
        lineTo(isoX, isoY + 3f)
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
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                strokeCap = StrokeCap.Round
            )
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
            Text("🌱", style = MaterialTheme.typography.headlineMedium)
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
private fun HabitCard(
    habit: HabitEntity,
    completed: Boolean,
    onComplete: () -> Unit,
    onArchive: () -> Unit
) {
    val haptics = LocalHapticFeedback.current
    val checkScale by animateFloatAsState(
        targetValue = if (completed) 1.12f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "check_scale"
    )

    GlassSurface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        cornerRadius = LiquidGlassTokens.cardRadius,
        elevation = if (completed) 0.dp else 4.dp,
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
            Text(habit.emoji, style = MaterialTheme.typography.headlineMedium)
            Column(
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .weight(1f)
            ) {
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
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
    onOpenTask: () -> Unit
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
                    onComplete = { viewModel.completeTask(task.id) },
                    onArchive = { viewModel.archiveTask(task.id) }
                )
            }
        }
    }
}

@Composable
private fun TaskCard(
    task: TaskEntity,
    onComplete: () -> Unit,
    onArchive: () -> Unit
) {
    val haptics = LocalHapticFeedback.current
    GlassSurface(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = LiquidGlassTokens.cardRadius,
        elevation = if (task.completed) 0.dp else 4.dp,
        tint = if (task.completed) MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.8f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onComplete()
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = if (task.completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (task.completed) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onArchive) {
                Icon(Icons.Rounded.DeleteOutline, contentDescription = "Archive task")
            }
        }
    }
}

@Composable
private fun AllTasksScreen(
    tasks: List<TaskEntity>,
    viewModel: RoutineViewModel,
    onOpenTask: () -> Unit
) {
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
                        "${tasks.count { !it.completed }} open · ${tasks.count { it.completed }} complete",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                FilledIconButton(onClick = onOpenTask) {
                    Icon(Icons.Rounded.Add, contentDescription = "Add task")
                }
            }
        }
        if (tasks.isEmpty()) {
            item {
                GlassSurface(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = LiquidGlassTokens.cardRadius,
                    elevation = 0.dp
                ) {
                    Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Your task list is clear", style = MaterialTheme.typography.titleMedium)
                        Text("Add the next meaningful action when you are ready.")
                    }
                }
            }
        } else {
            items(tasks, key = { it.id }) { task ->
                TaskCard(
                    task = task,
                    onComplete = { viewModel.completeTask(task.id) },
                    onArchive = { viewModel.archiveTask(task.id) }
                )
            }
        }
    }
}

@Composable
private fun PlannerScreen(tasks: List<TaskEntity>) {
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
                PlannerBlockCard(block.start, block.end, block.task.title, block.task.category)
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
private fun PlannerBlockCard(start: LocalDateTime, end: LocalDateTime, title: String, category: String) {
    GlassSurface(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = LiquidGlassTokens.smallRadius,
        elevation = 0.dp
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(
                    "${start.format(DateTimeFormatter.ofPattern("hh:mm a"))} - ${end.format(DateTimeFormatter.ofPattern("hh:mm a"))}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(category, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun ProgressScreen(state: TodayUiState) {
    AdaptiveLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (state.currentStreak == 0) "Start a new streak today"
                                else "${state.currentStreak} day streak 🔥",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "${state.activity.values.sum()} check-ins",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    ContributionHeatmap(state.activity)
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
                val barHeight = size.height * value.toFloat() / maxValue
                val left = index * slot + (slot - barWidth) / 2f
                drawRoundRect(
                    color = primaryColor.copy(alpha = .12f),
                    topLeft = Offset(left, 0f),
                    size = Size(barWidth, size.height),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
                )
                drawRoundRect(
                    color = primaryColor.copy(
                        alpha = .45f + .55f * value.toFloat() / maxValue
                    ),
                    topLeft = Offset(left, size.height - barHeight),
                    size = Size(barWidth, barHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
                )
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
            drawPath(
                path = path,
                color = secondaryColor,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        points.forEach { point ->
            drawCircle(surfaceColor, 5.dp.toPx(), point)
            drawCircle(secondaryColor, 3.dp.toPx(), point)
        }
    }
}

@Composable
private fun ContributionHeatmap(activity: Map<LocalDate, Int>) {
    val start = LocalDate.now().minusDays(111)
    val max = activity.values.maxOrNull()?.coerceAtLeast(1) ?: 1
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(7) { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(16) { column ->
                    val date = start.plusDays((column * 7L + row).coerceAtMost(111))
                    val intensity = (activity[date] ?: 0).toFloat() / max
                    Box(
                        Modifier
                            .size(15.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(heatmapColor(intensity))
                            .semantics {
                                contentDescription = "$date: ${activity[date] ?: 0} completions"
                            }
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
    onReducedMotionChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
                    }
                )
            }
        }

        GlassSurface(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 22.dp,
            highlighted = false,
            elevation = 0.dp
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
                    }
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
                    }
                )
            }
        }

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
                }
            )
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
    onDismiss: () -> Unit,
    onAdd: (String, String, String, Int?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Wellness") }
    var emoji by remember { mutableStateOf("✨") }
    var reminder by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add New Habit",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Quick add templates",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        PRESET_HABITS.forEach { preset ->
                            SuggestionChip(
                                onClick = {
                                    onAdd(preset.name, preset.category, preset.emoji, preset.reminderMinutes)
                                },
                                label = { Text("${preset.emoji} ${preset.name}") }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Habit name") },
                    placeholder = { Text("e.g. Read 15 pages") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Category & Icon",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
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
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = emoji,
                        onValueChange = { emoji = it.take(2) },
                        label = { Text("Emoji") },
                        modifier = Modifier.width(90.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = reminder,
                    onValueChange = { reminder = it.take(5) },
                    label = { Text("Daily reminder (HH:mm, optional)") },
                    placeholder = { Text("08:30") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
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
                    )
            ) {
                Text("Add habit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String, Int, Int?, Long?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Personal") }
    var estimate by remember { mutableStateOf("25") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Task") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = estimate,
                        onValueChange = { estimate = it.filter { char -> char.isDigit() }.take(3) },
                        label = { Text("Minutes") },
                        modifier = Modifier.width(100.dp),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
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
                enabled = title.isNotBlank()
            ) { Text("Add task") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
