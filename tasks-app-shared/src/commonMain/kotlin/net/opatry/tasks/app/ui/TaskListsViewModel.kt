/*
 * Copyright (c) 2024 Olivier Patry
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.opatry.tasks.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import net.opatry.tasks.app.ui.model.TaskListUIModel
import net.opatry.tasks.app.ui.model.TaskUIModel
import net.opatry.tasks.data.TaskRepository
import net.opatry.tasks.data.model.TaskDataModel
import net.opatry.tasks.data.model.TaskListDataModel

private fun TaskListDataModel.asTaskListUIModel(): TaskListUIModel {
    // TODO children
    // TODO date formatter
    return TaskListUIModel(
        id = id,
        title = title,
        lastUpdate = lastUpdate.toString(),
        tasks = tasks.map(TaskDataModel::asTaskUIModel)
    )
}

private fun TaskDataModel.asTaskUIModel(): TaskUIModel {
    return TaskUIModel(
        id = id,
        title = title,
        notes = notes,
        dueDate = dueDate?.toLocalDateTime(TimeZone.currentSystemDefault())?.date,
        isCompleted = isCompleted,
        position = position,
        indent = indent,
    )
}

class TaskListsViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val taskLists: Flow<List<TaskListUIModel>> = taskRepository.getTaskLists().mapLatest { allLists ->
        allLists.map(TaskListDataModel::asTaskListUIModel)
    }.shareIn(viewModelScope, started = SharingStarted.Lazily, replay = 1)

    init {
        // cold flow?
        viewModelScope.launch {
            try {
                taskRepository.sync()
            } catch (e: Exception) {
                // most likely no network
            }
        }
    }

    fun fetch() {
        viewModelScope.launch {
            refresh()
        }
    }

    private suspend fun refresh() {
        try {
            taskRepository.sync()
        } catch (e: Exception) {
            // most likely no network
        }
    }

    fun createTaskList(title: String) {
        viewModelScope.launch {
            try {
                taskRepository.createTaskList(title)
            } catch (e: Exception) {
                println("Error creating task list: $e")
                // TODO error handling
            }
        }
    }

    fun deleteTaskList(taskList: TaskListUIModel) {
        viewModelScope.launch {
            try {
                taskRepository.deleteTaskList(taskList.id)
            } catch (e: Exception) {
                println("Error while deleting task list (${taskList.id}): $e")
                // TODO error handling
            }
        }
    }

    fun renameTaskList(taskList: TaskListUIModel, newTitle: String) {
        viewModelScope.launch {
            try {
                taskRepository.renameTaskList(taskList.id, newTitle.trim())
            } catch (e: Exception) {
                println("Error creating task: $e")
                // TODO error handling
            }
        }
    }

    fun clearTaskListCompletedTasks(taskList: TaskListUIModel) {
        viewModelScope.launch {
            try {
                taskRepository.clearTaskListCompletedTasks(taskList.id)
            } catch (e: Exception) {
                println("Error creating task: $e")
                // TODO error handling
            }
        }
    }

    fun createTask(taskList: TaskListUIModel, title: String, notes: String = "", dueDate: LocalDate? = null) {
        viewModelScope.launch {
            try {
                taskRepository.createTask(taskList.id, title, notes, dueDate?.atStartOfDayIn(TimeZone.currentSystemDefault()))
            } catch (e: Exception) {
                println("Error while creating task: $e")
                // TODO error handling
            }
        }
    }

    fun deleteTask(task: TaskUIModel) {
        viewModelScope.launch {
            try {
                taskRepository.deleteTask(task.id)
            } catch (e: Exception) {
                println("Error while deleting task: $e")
                // TODO error handling
            }
        }
    }

    fun confirmTaskDeletion(task: TaskUIModel) {
        // TODO(?) purge pending deletion?
    }

    fun restoreTask(task: TaskUIModel) {
        viewModelScope.launch {
            try {
                taskRepository.restoreTask(task.id)
            } catch (e: Exception) {
                println("Error while restoring task: $e")
                // TODO error handling
            }
        }
    }

    fun toggleTaskCompletionState(task: TaskUIModel) {
        viewModelScope.launch {
            try {
                taskRepository.toggleTaskCompletionState(task.id)
            } catch (e: Exception) {
                println("Error while toggling task: $e")
                // TODO error handling
            }
        }
    }

    fun updateTask(task: TaskUIModel, title: String, notes: String, dueDate: LocalDate?) {
        viewModelScope.launch {
            try {
                taskRepository.updateTask(
                    task.id,
                    title.trim(),
                    notes.trim(),
                    dueDate?.atStartOfDayIn(TimeZone.currentSystemDefault())
                )
            } catch (e: Exception) {
                println("Error while updating task: $e")
                // TODO error handling
            }
        }
    }

    fun updateTaskTitle(task: TaskUIModel, title: String) {
        viewModelScope.launch {
            try {
                taskRepository.updateTaskTitle(task.id, title.trim())
            } catch (e: Exception) {
                println("Error while updating task title: $e")
                // TODO error handling
            }
        }
    }

    fun updateTaskNotes(task: TaskUIModel, notes: String) {
        viewModelScope.launch {
            try {
                taskRepository.updateTaskNotes(task.id, notes.trim())
            } catch (e: Exception) {
                println("Error while updating task notes: $e")
                // TODO error handling
            }
        }
    }

    fun updateTaskDueDate(task: TaskUIModel, dueDate: LocalDate?) {
        viewModelScope.launch {
            try {
                taskRepository.updateTaskDueDate(task.id, dueDate?.atStartOfDayIn(TimeZone.currentSystemDefault()))
            } catch (e: Exception) {
                println("Error while updating task due date: $e")
                // TODO error handling
            }
        }
    }

    fun unindentTask(task: TaskUIModel) {
        viewModelScope.launch {
            try {
                taskRepository.unindentTask(task.id)
            } catch (e: Exception) {
                println("Error while indenting task: $e")
                // TODO error handling
            }
        }
    }

    fun indentTask(task: TaskUIModel) {
        viewModelScope.launch {
            try {
                taskRepository.indentTask(task.id)
            } catch (e: Exception) {
                println("Error while indenting task: $e")
                // TODO error handling
            }
        }
    }

    fun moveToTop(task: TaskUIModel) {
        viewModelScope.launch {
            try {
                taskRepository.moveToTop(task.id)
            } catch (e: Exception) {
                println("Error while moving task: $e")
                // TODO error handling
            }
        }
    }

    fun moveToList(task: TaskUIModel, targetTaskList: TaskListUIModel) {
        viewModelScope.launch {
            try {
                taskRepository.moveToList(task.id, targetTaskList.id)
            } catch (e: Exception) {
                println("Error while moving task: $e")
                // TODO error handling
            }
        }
    }

    fun moveToNewList(task: TaskUIModel, targetTaskListTitle: String) {
        viewModelScope.launch {
            try {
                taskRepository.moveToNewList(task.id, targetTaskListTitle.trim())
            } catch (e: Exception) {
                println("Error while moving task: $e")
                // TODO error handling
            }
        }
    }
}
