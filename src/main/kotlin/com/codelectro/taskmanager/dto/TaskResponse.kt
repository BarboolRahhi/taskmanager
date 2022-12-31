package com.codelectro.taskmanager.dto

import com.codelectro.taskmanager.dto.project.ProjectResponse
import com.codelectro.taskmanager.model.Priority
import com.codelectro.taskmanager.model.Status
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class TaskResponse(
        var id: Int?,
        var title: String,
        var description: String,
        var status: Status,
        var priority: Priority,
        @JsonFormat(pattern = "EEE, d MMM yyyy HH:mm:ss")
        var createdAt: LocalDateTime,
        @JsonFormat(pattern = "EEE, d MMM yyyy HH:mm:ss")
        var completedAt: LocalDateTime?,
//        var project: ProjectResponse
)