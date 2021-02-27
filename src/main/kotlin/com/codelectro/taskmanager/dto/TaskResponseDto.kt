package com.codelectro.taskmanager.dto

import com.codelectro.taskmanager.model.Priority
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class TaskResponseDto(
        var id: Int?,
        var title: String,
        var description: String,
        var completed: Boolean = false,
        var priority: Priority,
        @JsonFormat(pattern = "EEE, d MMM yyyy HH:mm:ss")
        var createdAt: LocalDateTime,
        var category: CategoryDto
)