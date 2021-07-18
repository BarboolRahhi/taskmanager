package com.codelectro.taskmanager.service

import com.codelectro.taskmanager.dto.ProjectDto
import com.codelectro.taskmanager.dto.TaskRequest
import com.codelectro.taskmanager.dto.TaskResponse
import com.codelectro.taskmanager.dto.UserDto
import com.codelectro.taskmanager.model.Project
import com.codelectro.taskmanager.model.Status
import com.codelectro.taskmanager.model.Task
import com.codelectro.taskmanager.model.User
import java.time.LocalDateTime

fun User.toUserDto() = UserDto(
        id = id,
        username = username,
        email = email,
        imageURL = imageURL
)
fun ProjectDto.toProject(user: User) = Project(id, name, description, user = user)

fun Project.toProjectDto() = ProjectDto(id, name, description)

fun Project.toUpdate(projectDto: ProjectDto) = this.copy(
        name = projectDto.name,
        description = projectDto.description,
)

fun TaskRequest.toTask(project: Project, user: User) = Task(
        title = title,
        description = description,
        priority = priority,
        project = project,
        user = user
)

fun Task.toTaskResponseDto() = TaskResponse(
        id, title, description, status, priority, createdAt, completedAt, project.toProjectDto()
)

fun Task.toUpdate(dto: TaskRequest) = this.copy(
        title = dto.title,
        description = dto.description,
        status = dto.status ?: this.status,
        completedAt = if (dto.status === Status.DONE) LocalDateTime.now() else null,
        priority = dto.priority,
)