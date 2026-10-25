package com.routineflow.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.routineflow.data.local.TaskEntity
import com.routineflow.ui.components.GlassSurface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.routineflow.domain.TaskRepository
import com.routineflow.domain.RoutineRepository
import com.routineflow.settings.Preferences
import androidx.lifecycle.compose.collectAsStateWithLifecycle

data class OnboardingCategory(
    val id: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

data class TaskTemplate(
    val title: String,
    val category: String
)

val onboardingCategories = listOf(
    OnboardingCategory("study", "College & studying", androidx.compose.material.icons.Icons.Rounded.AutoAwesome),
    OnboardingCategory("productivity", "Personal productivity", androidx.compose.material.icons.Icons.Rounded.Check),
    OnboardingCategory("health", "Health & fitness", androidx.compose.material.icons.Icons.Rounded.PlayArrow),
    OnboardingCategory("habits", "Habits & routines", androidx.compose.material.icons.Icons.Rounded.PlayArrow),
    OnboardingCategory("coding", "Projects & coding", androidx.compose.material.icons.Icons.Rounded.Edit),
    OnboardingCategory("learning", "Reading & learning", androidx.compose.material.icons.Icons.Rounded.PlayArrow),
    OnboardingCategory("career", "Work & career", androidx.compose.material.icons.Icons.Rounded.AutoAwesome),
    OnboardingCategory("life", "Personal life & errands", androidx.compose.material.icons.Icons.Rounded.Check)
)

private val templates = mapOf(
    "study" to listOf("Revise today's lecture notes.", "Study a topic for 30 minutes.", "Solve five practice questions."),
    "productivity" to listOf("Plan my day.", "Review my top three priorities.", "Finish one important pending task."),
    "health" to listOf("Take a 20-minute walk.", "Stretch for 10 minutes.", "Plan a healthy meal."),
    "habits" to listOf("Read for 10 minutes.", "Write a short journal entry.", "Prepare for tomorrow."),
    "coding" to listOf("Work on my current project for 30 minutes.", "Fix one outstanding bug.", "Commit a completed piece of work."),
    "learning" to listOf("Read five pages.", "Complete one lesson.", "Take notes on something I learned."),
    "career" to listOf("Review today's work priorities.", "Practise an interview question.", "Learn one professional skill."),
    "life" to listOf("Organize my workspace.", "Plan an errand.", "Call someone important to me.")
)

fun onboardingTaskTemplates(selectedCategories: Set<String>): List<TaskTemplate> =
    selectedCategories.flatMap { key ->
        templates[key].orEmpty().map {
            TaskTemplate(
                title = it,
                category = onboardingCategories.firstOrNull { category -> category.id == key }?.label ?: "Personal"
            )
        }
    }.distinctBy { it.title }

data class OnboardingUiState(
    val completed: Boolean = false,
    val saving: Boolean = false,
    val error: String? = null,
    val reducedMotion: Boolean = false
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferences: Preferences,
    private val taskRepository: TaskRepository,
    routineRepository: RoutineRepository
) : ViewModel() {
    private val _saving = kotlinx.coroutines.flow.MutableStateFlow(false)
    private val _error = kotlinx.coroutines.flow.MutableStateFlow<String?>(null)
    val reducedMotion: StateFlow<Boolean> = preferences.reducedMotion
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)
    val darkTheme: StateFlow<Boolean?> = preferences.darkTheme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val appLockEnabled: StateFlow<Boolean> = preferences.appLockEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)
    val state: StateFlow<OnboardingUiState> = kotlinx.coroutines.flow.combine(
        preferences.onboardingCompleted,
        routineRepository.habits(),
        taskRepository.tasks(),
        _saving,
        _error
    ) { completed, habits, tasks, saving, error ->
        OnboardingUiState(
            completed = completed || habits.isNotEmpty() || tasks.isNotEmpty(),
            saving = saving,
            error = error,
            reducedMotion = reducedMotion.value
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), OnboardingUiState())

    fun setReducedMotion(enabled: Boolean) {
        viewModelScope.launch { preferences.setReducedMotion(enabled) }
    }

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch { preferences.setDarkTheme(enabled) }
    }

    fun setAppLockEnabled(enabled: Boolean) {
        viewModelScope.launch { preferences.setAppLockEnabled(enabled) }
    }

    fun finish(categories: Set<String>, selectedTasks: List<TaskTemplate>, onSaved: () -> Unit) {
        if (state.value.saving) return
        viewModelScope.launch {
            try {
                _saving.value = true
                val starterTasks = selectedTasks
                    .map { TaskEntity(title = it.title.trim(), category = it.category) }
                    .filter { it.title.isNotBlank() }
                taskRepository.addTasksIfMissing(starterTasks)
                preferences.completeOnboarding(categories)
                onSaved()
            } catch (_: Exception) {
                // The caller remains on the confirmation screen and can retry safely.
                _error.value = "We couldn't save your plan. Please try again."
            } finally {
                _saving.value = false
            }
        }
    }

}

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val categories = remember { mutableStateMapOf<String, Boolean>() }
    var page by remember { mutableStateOf(0) }
    var selected by remember { mutableStateOf<List<TaskTemplate>>(emptyList()) }
    var customTask by remember { mutableStateOf("") }
    var editing by remember { mutableStateOf<TaskTemplate?>(null) }
    var editValue by remember { mutableStateOf("") }
    val onboardingState by viewModel.state.collectAsStateWithLifecycle()
    val totalPages = 5

    Box(
        Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .navigationBarsPadding()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp)
                .align(Alignment.Center)
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "RoutineFlow", 
                    style = MaterialTheme.typography.titleLarge, 
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            
            LinearProgressIndicator(
                progress = { (page + 1) / totalPages.toFloat() },
                Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceContainerHighest
            )
            
            AnimatedContent(
                targetState = page,
                modifier = Modifier.weight(1f),
                transitionSpec = {
                    (fadeIn() + androidx.compose.animation.slideInHorizontally { it / 4 } + androidx.compose.animation.scaleIn(initialScale = 0.95f)) togetherWith
                    (fadeOut() + androidx.compose.animation.slideOutHorizontally { -it / 4 } + androidx.compose.animation.scaleOut(targetScale = 0.95f))
                },
                label = "onboarding_page"
            ) { current ->
                when (current) {
                    0 -> IntroPage()
                    1 -> CategoryPage(
                        selected = categories.keys,
                        onToggle = { key ->
                            if (categories[key] == true) categories.remove(key) else categories[key] = true
                        },
                        onSkip = { page = 2 },
                        onNext = { page = 2 }
                    )
                    2 -> TaskSetupPage(
                        categories = categories.keys,
                        selected = selected,
                        customTask = customTask,
                        onCustomTaskChange = { customTask = it },
                        onToggle = { template ->
                            selected = if (template in selected) selected - template else selected + template
                        },
                        onAddCustom = {
                            if (customTask.isNotBlank()) {
                                selected = selected + TaskTemplate(customTask.trim(), "Personal")
                                customTask = ""
                            }
                        },
                        onEdit = {
                            if (it !in selected) selected = selected + it
                            editing = it
                            editValue = it.title
                        },
                        onSkip = { selected = emptyList(); page = 4 },
                        onNext = { page = 3 }
                    )
                    3 -> PlanPage(
                        selected = selected,
                        onEdit = { page = 2 },
                        onConfirm = { page = 4 },
                        saving = false
                    )
                    else -> WelcomePage(
                        selected = selected,
                        saving = onboardingState.saving,
                        error = onboardingState.error,
                        onOpen = { viewModel.finish(categories.keys, selected, onFinished) }
                    )
                }
            }
            if (page == 0) {
                Button(
                    onClick = { page = 1 },
                    Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 18.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Get started", fontWeight = FontWeight.Bold)
                    Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null)
                }
            }
        }
    }
    
    editing?.let { current ->
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { editing = null },
            title = { Text("Edit suggestion", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    editValue, 
                    { editValue = it }, 
                    label = { Text("Task") }, 
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editValue.isNotBlank()) {
                            selected = selected.map {
                                if (it == current) it.copy(title = editValue.trim()) else it
                            }
                        }
                        editing = null
                    },
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Done", fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { editing = null }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
}

@Composable
private fun IntroPage() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlassSurface(
            Modifier.fillMaxWidth().padding(bottom = 36.dp),
            cornerRadius = 32.dp,
            elevation = 12.dp,
            tint = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
        ) {
            Column(
                Modifier.fillMaxWidth().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Icon(
                    Icons.Rounded.AutoAwesome, 
                    null, 
                    Modifier.size(72.dp), 
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Clarity for the next step",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
                androidx.compose.material3.Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                ) {
                    Text(
                        "✓  Today  ·  3 tasks  ·  40 XP", 
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
        Text(
            "Make space for what matters.", 
            style = MaterialTheme.typography.headlineLarge, 
            fontWeight = FontWeight.ExtraBold, 
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            "Your tasks, routines, and goals — organized into a plan that works for your life.", 
            Modifier.padding(top = 16.dp), 
            textAlign = TextAlign.Center, 
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun CategoryPage(
    selected: Set<String>,
    onToggle: (String) -> Unit,
    onSkip: () -> Unit,
    onNext: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("What matters to you?", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
        Text("Pick any areas you want to organize. You can change this later.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyLarge)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 12.dp)) {
            items(onboardingCategories, key = { it.id }) { category ->
                val checked = category.id in selected
                GlassSurface(
                    modifier = Modifier.fillMaxWidth().clickable { onToggle(category.id) }.semantics { role = Role.Checkbox; contentDescription = category.label },
                    cornerRadius = 16.dp,
                    elevation = if (checked) 2.dp else 4.dp,
                    tint = if (checked) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                ) {
                    Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(category.icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                        Text(
                            category.label, 
                            Modifier.weight(1f).padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = if (checked) FontWeight.Bold else FontWeight.SemiBold,
                            color = if (checked) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                        )
                        if (checked) Icon(Icons.Rounded.Check, null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onSkip) { Text("Skip for now", color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.SemiBold) }
            Button(
                onClick = onNext,
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) { Text("Continue", fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
private fun TaskSetupPage(
    categories: Set<String>,
    selected: List<TaskTemplate>,
    customTask: String,
    onCustomTaskChange: (String) -> Unit,
    onToggle: (TaskTemplate) -> Unit,
    onAddCustom: () -> Unit,
    onEdit: (TaskTemplate) -> Unit,
    onSkip: () -> Unit,
    onNext: () -> Unit
) {
    val suggestions = onboardingTaskTemplates(categories)
    LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp), contentPadding = PaddingValues(bottom = 12.dp)) {
        item {
            Text("Let's build your first plan.", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
            Text("Start with a few suggestions, customize them, or add your own tasks.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyLarge)
            Text("${selected.size} tasks selected", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
        }
        items(suggestions, key = { it.title }) { template ->
            TaskTemplateRow(template, template in selected, onToggle, onEdit)
        }
        item {
            GlassSurface(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                cornerRadius = 20.dp,
                elevation = 2.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {
                    OutlinedTextField(
                        value = customTask,
                        onValueChange = onCustomTaskChange,
                        modifier = Modifier.weight(1f),
                        label = { Text("Add your own task") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
                            focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        )
                    )
                    Spacer(Modifier.width(12.dp))
                    Button(
                        onClick = onAddCustom, 
                        modifier = Modifier.size(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(Icons.Rounded.Check, contentDescription = "Add custom task")
                    }
                }
            }
        }
        item {
            Row(Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onSkip) { Text("Skip for now", color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.SemiBold) }
                Button(
                    onClick = onNext,
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                ) { Text("Review plan", fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Composable
private fun TaskTemplateRow(
    template: TaskTemplate,
    selected: Boolean,
    onToggle: (TaskTemplate) -> Unit,
    onEdit: (TaskTemplate) -> Unit
) {
    GlassSurface(
        modifier = Modifier.fillMaxWidth().clickable { onToggle(template) }.semantics { contentDescription = template.title },
        cornerRadius = 16.dp,
        elevation = if (selected) 0.dp else 4.dp,
        tint = if (selected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(if (selected) Icons.Rounded.Check else Icons.Rounded.PlayArrow, null, tint = MaterialTheme.colorScheme.primary)
            Text(
                template.title, 
                Modifier.weight(1f).padding(horizontal = 14.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
            )
            IconButton(onClick = { onEdit(template) }) {
                Icon(Icons.Rounded.Edit, contentDescription = "Edit ${template.title}", tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun PlanPage(
    selected: List<TaskTemplate>,
    onEdit: () -> Unit,
    onConfirm: () -> Unit,
    saving: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Your first plan", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
        Text(
            if (selected.isEmpty()) "No tasks will be added. You can start whenever you are ready."
            else "${selected.size} task${if (selected.size == 1) "" else "s"} ready to begin.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )
        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(selected) { task ->
                GlassSurface(Modifier.fillMaxWidth(), cornerRadius = 16.dp, elevation = 2.dp) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Check, null, tint = MaterialTheme.colorScheme.primary)
                        Text(
                            task.title, 
                            Modifier.weight(1f).padding(horizontal = 14.dp),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        androidx.compose.material3.Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerHighest
                        ) {
                            Text(
                                task.category, 
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onEdit, enabled = !saving) { Text("Edit tasks", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold) }
            Button(
                onClick = onConfirm, 
                enabled = !saving,
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(if (saving) "Saving…" else "Use this plan", fontWeight = FontWeight.Bold)
            }
        }

    }
}

@Composable
private fun WelcomePage(
    selected: List<TaskTemplate>,
    saving: Boolean,
    error: String?,
    onOpen: () -> Unit
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlassSurface(
            modifier = Modifier.padding(bottom = 32.dp),
            cornerRadius = 32.dp,
            elevation = 16.dp,
            tint = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
        ) {
            Box(Modifier.padding(32.dp)) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(80.dp), tint = MaterialTheme.colorScheme.primary)
            }
        }
        Text("You're ready to begin.", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
        Text(
            "Your next step doesn't need to be perfect. It just needs to be yours.",
            Modifier.padding(top = 16.dp, start = 24.dp, end = 24.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )
        androidx.compose.material3.Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text(
                if (selected.isEmpty()) "Start with a blank slate and add a task whenever you're ready."
                else "${selected.size} task${if (selected.size == 1) "" else "s"} prepared for your first plan.",
                Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
        error?.let {
            Text(it, Modifier.padding(top = 16.dp), color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = onOpen,
            enabled = !saving,
            modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
            contentPadding = PaddingValues(vertical = 18.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(if (saving) "Saving…" else "Open RoutineFlow", fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleLarge)
        }
    }
}
