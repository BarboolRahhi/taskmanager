package com.codelectro.taskmanager.service

import com.codelectro.taskmanager.dto.TaskRequest
import com.codelectro.taskmanager.dto.TaskResponse
import com.codelectro.taskmanager.dto.UserDto
import com.codelectro.taskmanager.dto.project.ProjectRequest
import com.codelectro.taskmanager.dto.project.ProjectResponse
import com.codelectro.taskmanager.dto.project.TaskDetails
import com.codelectro.taskmanager.model.*
import java.time.LocalDateTime

fun User.toUserDto() = UserDto(
    id = id,
    username = username,
    email = email,
    imageURL = imageURL
)

fun ProjectRequest.toProjectEntity(user: User) = Project(null, name, description, startDate, dueDate, isCompleted, user)

fun Project.toProjectResponse(taskStatus: TaskStatus?) =
    ProjectResponse(
        id, name, description, startDate, dueDate, isCompleted, toTaskDetails(taskStatus)
    )
fun toTaskDetails(taskStatus: TaskStatus?) =
    TaskDetails(
        totalTask = taskStatus?.getTotalTask() ?: 0,
        countOfTodo = taskStatus?.getCountOfTodoTask() ?: 0,
        countOfInProgress = taskStatus?.getCountOfInProgressTask() ?: 0,
        countOfDone = taskStatus?.getCountOfDoneTask() ?: 0,
        progress = taskStatus?.getProgress() ?: 0
    )

fun TaskStatus.getProgress(): Int {
    return (getCountOfDoneTask() / getTotalTask().toDouble() * 100).toInt()
}

fun Project.toUpdate(projectRequest: ProjectRequest) = this.apply {
    name = projectRequest.name
    description = projectRequest.description
    isCompleted = projectRequest.isCompleted ?: isCompleted
    dueDate = projectRequest.dueDate
}

fun TaskRequest.toTask(project: Project, user: User) = Task(
    title = title,
    description = description,
    priority = priority,
    project = project,
    user = user
)

fun Task.toTaskResponseDto() = TaskResponse(
    id, title, description, status, priority, createdAt, completedAt
)

fun Task.toUpdate(dto: TaskRequest) = this.copy(
    title = dto.title,
    description = dto.description,
    status = dto.status ?: this.status,
    completedAt = if (dto.status === Status.DONE) LocalDateTime.now() else null,
    priority = dto.priority,
)