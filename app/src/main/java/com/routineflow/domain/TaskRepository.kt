package com.routineflow.domain

import com.routineflow.data.local.SubtaskEntity
import com.routineflow.data.local.TaskDao
import com.routineflow.data.local.TaskEntity
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class TaskRepository @Inject constructor(
    private val dao: TaskDao
) {
    fun tasks(): Flow<List<TaskEntity>> = dao.observeTasks()

    fun overdueTasks(now: Long = System.currentTimeMillis()): Flow<List<TaskEntity>> =
        dao.observeOverdueTasks(now)

    fun subtasks(taskId: Long): Flow<List<SubtaskEntity>> = dao.observeSubtasks(taskId)

    suspend fun addTask(task: TaskEntity): Long = dao.insertTask(task)

    suspend fun addTasksIfMissing(tasks: List<TaskEntity>) {
        tasks.forEach { task ->
            if (dao.findActiveTask(task.title, task.category) == null) {
                dao.insertTask(task)
            }
        }
    }

    suspend fun complete(taskId: Long): Boolean =
        dao.completeTask(taskId, LocalDate.now().toString())

    suspend fun archive(taskId: Long) =
        dao.archiveTask(taskId, System.currentTimeMillis())

    suspend fun addSubtask(taskId: Long, title: String, position: Int) =
        dao.insertSubtask(SubtaskEntity(taskId = taskId, title = title, position = position))

    suspend fun setSubtaskCompleted(subtaskId: Long, completed: Boolean) =
        dao.updateSubtaskCompletion(subtaskId, completed)
}
