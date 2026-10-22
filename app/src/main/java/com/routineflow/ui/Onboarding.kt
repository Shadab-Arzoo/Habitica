package com.routineflow.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
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
    val emoji: String
)

data class TaskTemplate(
    val title: String,
    val category: String
)

val onboardingCategories = listOf(
    OnboardingCategory("study", "College & studying", "📚"),
    OnboardingCategory("productivity", "Personal productivity", "✨"),
    OnboardingCategory("health", "Health & fitness", "🌿"),
    OnboardingCategory("habits", "Habits & routines", "🔁"),
    OnboardingCategory("coding", "Projects & coding", "💻"),
    OnboardingCategory("learning", "Reading & learning", "🧠"),
    OnboardingCategory("career", "Work & career", "🚀"),
    OnboardingCategory("life", "Personal life & errands", "🏡")
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
    val introPages = 5

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
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("RoutineFlow", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                if (page < 5) TextButton(onClick = { page = 5 }) { Text("Skip") }
            }
            LinearProgressIndicator(
                progress = { (page + 1) / 9f },
                Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )
            AnimatedContent(
                targetState = page,
                modifier = Modifier.weight(1f),
                label = "onboarding_page"
            ) { current ->
                when {
                    current < introPages -> IntroPage(current)
                    current == 5 -> CategoryPage(
                        selected = categories.keys,
                        onToggle = { key ->
                            if (categories[key] == true) categories.remove(key) else categories[key] = true
                        },
                        onSkip = { page = 6 },
                        onNext = { page = 6 }
                    )
                    current == 6 -> TaskSetupPage(
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
                        onSkip = { selected = emptyList(); page = 8 },
                        onNext = { page = 7 }
                    )
                    current == 7 -> PlanPage(
                        selected = selected,
                        onEdit = { page = 6 },
                        onConfirm = { page = 8 },
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
            if (page < 5) {
                Button(
                    onClick = { page++ },
                    Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 15.dp)
                ) {
                    Text(if (page == 0) "Get started" else "Continue")
                    Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null)
                }
            }
        }
    }
    editing?.let { current ->
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { editing = null },
            title = { Text("Edit suggestion") },
            text = {
                OutlinedTextField(editValue, { editValue = it }, label = { Text("Task") }, singleLine = true)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (editValue.isNotBlank()) {
                            selected = selected.map {
                                if (it == current) it.copy(title = editValue.trim()) else it
                            }
                        }
                        editing = null
                    }
                ) { Text("Done") }
            }
        )
    }
}

@Composable
private fun IntroPage(page: Int) {
    val content = listOf(
        "Make space for what matters." to "Your tasks, routines, and goals — organized into a plan that works for your life.",
        "Know what to do next." to "Organize your day, prioritize important tasks, and make progress one step at a time.",
        "Small wins add up." to "Earn XP, unlock achievements, and celebrate progress at your own pace.",
        "Fall behind? Start again." to "Recover with a realistic plan instead of an overwhelming backlog.",
        "Make RoutineFlow yours." to "Choose what matters to you. We'll help you start with a plan that fits your life."
    )[page]
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlassSurface(
            Modifier.fillMaxWidth().padding(bottom = 28.dp),
            cornerRadius = 32.dp,
            tint = MaterialTheme.colorScheme.primaryContainer.copy(alpha = .55f)
        ) {
            Column(
                Modifier.fillMaxWidth().padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(58.dp), tint = MaterialTheme.colorScheme.primary)
                Text(
                    when (page) { 0 -> "A calmer way to move forward"; 1 -> "Clarity for the next step"; 2 -> "Progress without pressure"; 3 -> "A fresh start is always possible"; else -> "Built around your life" },
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Text("✓  Today  ·  3 tasks  ·  40 XP", color = MaterialTheme.colorScheme.primary)
            }
        }
        Text(content.first, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text(content.second, Modifier.padding(top = 10.dp), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun CategoryPage(
    selected: Set<String>,
    onToggle: (String) -> Unit,
    onSkip: () -> Unit,
    onNext: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("What matters to you?", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Pick any areas you want to organize. You can change this later.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 8.dp)) {
            items(onboardingCategories, key = { it.id }) { category ->
                val checked = category.id in selected
                val color by animateColorAsState(
                    if (checked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer,
                    label = "category_color"
                )
                OutlinedCard(
                    onClick = { onToggle(category.id) },
                    modifier = Modifier.fillMaxWidth().semantics { role = Role.Checkbox; contentDescription = category.label },
                    colors = CardDefaults.outlinedCardColors(containerColor = color)
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(category.emoji, style = MaterialTheme.typography.titleLarge)
                        Text(category.label, Modifier.weight(1f).padding(horizontal = 14.dp))
                        if (checked) Icon(Icons.Rounded.Check, null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = onSkip) { Text("Skip for now") }
            Button(onClick = onNext) { Text("Continue") }
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
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 12.dp)) {
        item {
            Text("Let's build your first plan.", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Start with a few suggestions, customize them, or add your own tasks.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("${selected.size} tasks selected", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
        }
        items(suggestions, key = { it.title }) { template ->
            TaskTemplateRow(template, template in selected, onToggle, onEdit)
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = customTask,
                    onValueChange = onCustomTaskChange,
                    modifier = Modifier.weight(1f),
                    label = { Text("Add your own task") },
                    singleLine = true
                )
                IconButton(onClick = onAddCustom, modifier = Modifier.size(52.dp)) {
                    Icon(Icons.Rounded.Check, contentDescription = "Add custom task")
                }
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = onSkip) { Text("Skip for now") }
                Button(onClick = onNext) { Text("Review plan") }
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
    OutlinedCard(
        onClick = { onToggle(template) },
        modifier = Modifier.fillMaxWidth().semantics { contentDescription = template.title },
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(if (selected) Icons.Rounded.Check else Icons.Rounded.PlayArrow, null, tint = MaterialTheme.colorScheme.primary)
            Text(template.title, Modifier.weight(1f).padding(horizontal = 10.dp))
            IconButton(onClick = { onEdit(template) }) {
                Icon(Icons.Rounded.Edit, contentDescription = "Edit ${template.title}")
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
        Text("Your first plan", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(
            if (selected.isEmpty()) "No tasks will be added. You can start whenever you are ready."
            else "${selected.size} task${if (selected.size == 1) "" else "s"} ready to begin.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(selected) { task ->
                GlassSurface(Modifier.fillMaxWidth(), cornerRadius = 16.dp, highlighted = false) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Check, null, tint = MaterialTheme.colorScheme.primary)
                        Text(task.title, Modifier.weight(1f).padding(horizontal = 10.dp))
                        Text(task.category, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = onEdit, enabled = !saving) { Text("Edit") }
            FilledTonalButton(onClick = onConfirm, enabled = !saving) {
                Text(if (saving) "Saving…" else "Use this plan")
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
        Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
        Text("You're ready to begin.", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text(
            "Your next step doesn't need to be perfect. It just needs to be yours.",
            Modifier.padding(top = 10.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            if (selected.isEmpty()) "Start with a blank slate and add a task whenever you're ready."
            else "${selected.size} task${if (selected.size == 1) "" else "s"} prepared for your first plan.",
            Modifier.padding(top = 18.dp),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
        error?.let {
            Text(it, Modifier.padding(top = 12.dp), color = MaterialTheme.colorScheme.error)
        }
        Button(
            onClick = onOpen,
            enabled = !saving,
            modifier = Modifier.fillMaxWidth().padding(top = 28.dp),
            contentPadding = PaddingValues(vertical = 15.dp)
        ) {
            Text(if (saving) "Saving…" else "Open RoutineFlow")
        }
    }
}
