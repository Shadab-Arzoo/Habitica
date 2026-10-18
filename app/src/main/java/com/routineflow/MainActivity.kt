package com.routineflow

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.routineflow.data.local.HabitEntity
import com.routineflow.notifications.ReminderReceiver
import com.routineflow.ui.RoutineViewModel
import com.routineflow.ui.TodayUiState
import com.routineflow.ui.components.GlassSurface
import com.routineflow.ui.theme.RoutineFlowTheme
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ReminderReceiver.createChannel(this)
        if (
            android.os.Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 42)
        }
        setContent { RoutineFlowRoot() }
    }
}

@Composable
private fun RoutineFlowRoot() {
    val systemDarkMode = isSystemInDarkTheme()
    var darkMode by remember { mutableStateOf(systemDarkMode) }
    RoutineFlowTheme(darkTheme = darkMode) {
        Box(modifier = Modifier.fillMaxSize()) {
            AmbientBackground(darkMode = darkMode)
            RoutineFlowApp(darkMode = darkMode, onThemeToggle = { darkMode = !darkMode })
        }
    }
}

/**
 * Soft ambient light orbs in pastel colors to create realistic refraction
 * through frosted glass surfaces.
 */
@Composable
private fun AmbientBackground(darkMode: Boolean) {
    val primaryGlow = if (darkMode) Color(0xFF7B61FF).copy(alpha = 0.18f) else Color(0xFF8B78FF).copy(alpha = 0.22f)
    val secondaryGlow = if (darkMode) Color(0xFF4AAFA3).copy(alpha = 0.14f) else Color(0xFF52B5A8).copy(alpha = 0.20f)
    val tertiaryGlow = if (darkMode) Color(0xFFE77BAF).copy(alpha = 0.12f) else Color(0xFFE57EA8).copy(alpha = 0.18f)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val glowSize = maxWidth.coerceIn(220.dp, 360.dp)
        // Top-Right Soft Purple Glow
        Box(
            modifier = Modifier
                .offset(x = maxWidth * .58f, y = -(glowSize * .12f))
                .size(glowSize)
                .clip(CircleShape)
                .background(primaryGlow)
                .blur(80.dp)
        )
        // Mid-Left Soft Mint/Sage Glow
        Box(
            modifier = Modifier
                .offset(x = -(glowSize * .35f), y = maxHeight * .30f)
                .size(glowSize * .8f)
                .clip(CircleShape)
                .background(secondaryGlow)
                .blur(90.dp)
        )
        // Bottom-Right Soft Pastel Rose Glow
        Box(
            modifier = Modifier
                .offset(x = maxWidth * .50f, y = maxHeight * .70f)
                .size(glowSize * .85f)
                .clip(CircleShape)
                .background(tertiaryGlow)
                .blur(85.dp)
        )
    }
}

@Composable
private fun RoutineFlowApp(
    darkMode: Boolean,
    onThemeToggle: () -> Unit,
    viewModel: RoutineViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showAdd by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    val haptics = LocalHapticFeedback.current

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            AnimatedVisibility(
                visible = selectedTab == 0,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                // Glassmorphic FAB with glowing ambient shadow
                Box(
                    modifier = Modifier
                        .padding(bottom = 6.dp, end = 6.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            )
                        )
                ) {
                    FloatingActionButton(
                        onClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            showAdd = true
                        },
                        shape = CircleShape,
                        containerColor = Color.Transparent,
                        contentColor = Color.White,
                        elevation = androidx.compose.material3.FloatingActionButtonDefaults.elevation(0.dp, 0.dp),
                        modifier = Modifier
                            .semantics { contentDescription = "Add a new habit" }
                            .padding(2.dp)
                    ) {
                        Icon(
                            Icons.Rounded.Add,
                            contentDescription = "Add habit",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                GlassSurface(
                    Modifier
                        .fillMaxWidth()
                        .widthIn(max = 640.dp)
                        .navigationBarsPadding()
                        .padding(horizontal = 22.dp, vertical = 10.dp),
                    cornerRadius = 32.dp,
                    elevation = 14.dp
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NavigationItem("Today", Icons.Rounded.Check, selectedTab == 0) {
                            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            selectedTab = 0
                        }
                        NavigationItem("Progress", Icons.Rounded.Insights, selectedTab == 1) {
                            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            selectedTab = 1
                        }
                        NavigationItem("Settings", Icons.Rounded.Settings, selectedTab == 2) {
                            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            selectedTab = 2
                        }
                    }
                }
            }
        }
    ) { padding ->
        AnimatedContent(
            targetState = selectedTab,
            label = "screen_transition",
            modifier = Modifier.fillMaxSize()
        ) { tab ->
            when (tab) {
                0 -> TodayScreen(state, viewModel, padding, onOpenAdd = { showAdd = true })
                1 -> ProgressScreen(state, padding)
                else -> SettingsScreen(darkMode, onThemeToggle, padding)
            }
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
}

@Composable
private fun TodayScreen(
    state: TodayUiState,
    viewModel: RoutineViewModel,
    padding: PaddingValues,
    onOpenAdd: () -> Unit
) {
    val horizontalPadding = if (LocalConfiguration.current.screenWidthDp < 600) 16.dp else 28.dp
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 760.dp)
                .padding(padding)
                .statusBarsPadding(),
            contentPadding = PaddingValues(
                start = horizontalPadding,
                end = horizontalPadding,
                top = 22.dp,
                bottom = 110.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        item {
            Column {
                Text(
                    "Good evening",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    LocalDate.now().format(
                        DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.getDefault())
                    ),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        item { MomentumCard(state) }
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
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Your habits",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                TextButton(onClick = onOpenAdd) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "Add more",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
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
                    Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onOpenAdd)
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            modifier = Modifier.size(56.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Rounded.Add,
                                    contentDescription = "Add habit",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        Text(
                            "Build your first rhythm",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Tap here or the + button below to add your first habit.",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                            fontSize = 14.sp
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
}

@Composable
private fun MomentumCard(state: TodayUiState) {
    val progress by animateFloatAsState(
        targetValue = state.progress,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "daily_progress"
    )
    GlassSurface(Modifier.fillMaxWidth(), cornerRadius = 28.dp, elevation = 12.dp) {
        Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Today’s momentum", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(
                        "${state.completedCount} of ${state.habits.size} complete",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                ) {
                    Row(
                        Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        AnimatedContent(targetState = state.xp, label = "xp_counter") { xp ->
                            Text(
                                "$xp XP",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
            )
        }
    }
}

@Composable
private fun RecoveryCard() {
    GlassSurface(
        Modifier.fillMaxWidth(),
        tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.14f),
        cornerRadius = 24.dp
    ) {
        Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("🌱", style = MaterialTheme.typography.headlineSmall)
            Column(Modifier.padding(start = 12.dp).weight(1f)) {
                Text("A gentle restart is available", fontWeight = FontWeight.SemiBold)
                Text(
                    "Small steps still move you forward.",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall
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
    GlassSurface(Modifier.fillMaxWidth().animateContentSize(), cornerRadius = 24.dp) {
        Row(
            Modifier
                .padding(16.dp)
                .semantics {
                    contentDescription = "${habit.name}, ${if (completed) "completed" else "not completed"}"
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(habit.emoji, style = MaterialTheme.typography.headlineSmall)
            Column(Modifier.padding(horizontal = 14.dp).weight(1f)) {
                Text(habit.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Spacer(Modifier.height(2.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
                ) {
                    Text(
                        habit.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
            Crossfade(targetState = completed, label = "habit_completion") { isComplete ->
                if (isComplete) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Rounded.Check,
                                contentDescription = "Completed",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
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
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Rounded.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
            IconButton(onClick = onArchive) {
                Icon(
                    Icons.Rounded.DeleteOutline,
                    contentDescription = "Archive ${habit.name}",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
private fun ProgressScreen(state: TodayUiState, padding: PaddingValues) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(padding)
            .statusBarsPadding(),
        contentPadding = PaddingValues(20.dp, 20.dp, 20.dp, 110.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Progress", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }
        item {
            GlassSurface(Modifier.fillMaxWidth(), cornerRadius = 28.dp) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Consistency", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Text(
                                if (state.currentStreak == 0) "Start a new streak today"
                                else "${state.currentStreak} day streak 🔥",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                            )
                        }
                        Text(
                            "${state.activity.values.sum()} check-ins",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    ContributionHeatmap(state.activity)
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Less", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                        Spacer(Modifier.width(4.dp))
                        repeat(4) { HeatmapLegend(it) }
                        Spacer(Modifier.width(4.dp))
                        Text("More", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    }
                }
            }
        }
        item {
            GlassSurface(Modifier.fillMaxWidth(), cornerRadius = 26.dp) {
                Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("This week", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(
                        "${(state.progress * 100).toInt()}% completion rate",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Keep showing up. Consistency grows one day at a time.",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        item {
            GlassSurface(Modifier.fillMaxWidth(), cornerRadius = 26.dp) {
                Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Level 1 · Steady starter", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text("${state.xp} / 100 XP", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                    LinearProgressIndicator(
                        progress = { (state.xp / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
                    )
                }
            }
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
private fun HeatmapLegend(level: Int) {
    Box(
        Modifier
            .padding(horizontal = 2.dp)
            .size(12.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(heatmapColor(level / 3f))
    )
}

@Composable
private fun heatmapColor(intensity: Float): Color {
    val base = MaterialTheme.colorScheme.primary
    return if (intensity <= 0f) {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    } else {
        base.copy(alpha = 0.28f + intensity.coerceIn(0f, 1f) * 0.72f)
    }
}

@Composable
private fun SettingsScreen(
    darkMode: Boolean,
    onThemeToggle: () -> Unit,
    padding: PaddingValues
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(padding)
            .statusBarsPadding()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        GlassSurface(Modifier.fillMaxWidth(), cornerRadius = 24.dp) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            if (darkMode) Icons.Rounded.DarkMode else Icons.Rounded.LightMode,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Column(Modifier.padding(horizontal = 14.dp).weight(1f)) {
                    Text("Appearance", fontWeight = FontWeight.SemiBold)
                    Text(
                        if (darkMode) "Dark frosted glass" else "Light frosted glass",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                GlassToggle(checked = darkMode, onCheckedChange = onThemeToggle)
            }
        }
        GlassSurface(Modifier.fillMaxWidth(), cornerRadius = 24.dp) {
            Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.NotificationsActive,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text("Gentle Reminders", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }
                Text(
                    "Receive daily inspirational quotes to keep your routine active.",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun GlassToggle(checked: Boolean, onCheckedChange: () -> Unit) {
    val trackColor by animateColorAsState(
        targetValue = if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
        label = "toggle_track"
    )
    Box(
        Modifier
            .size(width = 54.dp, height = 32.dp)
            .clip(CircleShape)
            .background(trackColor)
            .clickable(onClick = onCheckedChange)
            .semantics {
                contentDescription = if (checked) "Switch to light mode" else "Switch to dark mode"
            }
            .padding(4.dp)
    ) {
        Box(
            Modifier
                .size(24.dp)
                .align(if (checked) Alignment.CenterEnd else Alignment.CenterStart)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
        )
    }
}

@Composable
private fun NavigationItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val tint by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
        label = "nav_tint"
    )
    TextButton(onClick = onClick) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = label, tint = tint)
            Spacer(Modifier.height(2.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = tint,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
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

@Composable
private fun categoryAccentColor(category: String): Color {
    return when (category) {
        "Wellness" -> MaterialTheme.colorScheme.primary
        "Focus" -> MaterialTheme.colorScheme.secondary
        "Fitness" -> MaterialTheme.colorScheme.tertiary
        "Mindful" -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.secondary
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddHabitDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String, Int?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Wellness") }
    var emoji by remember { mutableStateOf("✨") }
    var reminder by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        GlassSurface(
            Modifier.fillMaxWidth(),
            cornerRadius = 32.dp,
            elevation = 16.dp
        ) {
            Column(
                Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Add New Habit", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("✨", fontSize = 16.sp)
                        }
                    }
                }

                // Quick Add Presets ("+ to add more")
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        "⚡ Quick Add Templates",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        PRESET_HABITS.forEach { preset ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                modifier = Modifier.clickable {
                                    onAdd(preset.name, preset.category, preset.emoji, preset.reminderMinutes)
                                }
                            ) {
                                Row(
                                    Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(preset.emoji, fontSize = 14.sp)
                                    Text("+ ${preset.name}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                }

                // Custom Inputs
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Habit Name") },
                        placeholder = { Text("e.g. Read 15 pages") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Category Selector Chips
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            "Category & Icon",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            CATEGORY_PRESETS.forEach { (catName, catEmoji) ->
                                val catColor = categoryAccentColor(catName)
                                val isSelected = category == catName
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) catColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
                                    modifier = Modifier.clickable {
                                        category = catName
                                        emoji = catEmoji
                                    }
                                ) {
                                    Row(
                                        Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(catEmoji, fontSize = 14.sp)
                                        Text(
                                            catName,
                                            fontSize = 12.sp,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = emoji,
                            onValueChange = { emoji = it.take(2) },
                            label = { Text("Emoji") },
                            modifier = Modifier.width(85.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                            )
                        )
                        OutlinedTextField(
                            value = category,
                            onValueChange = { category = it },
                            label = { Text("Category") },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                            )
                        )
                    }

                    OutlinedTextField(
                        value = reminder,
                        onValueChange = { reminder = it.take(5) },
                        label = { Text("Daily Reminder (HH:mm, optional)") },
                        placeholder = { Text("08:30") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                    Spacer(Modifier.width(8.dp))
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
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Add Habit", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
