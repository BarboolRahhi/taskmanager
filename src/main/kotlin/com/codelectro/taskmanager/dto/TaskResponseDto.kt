package com.codelectro.taskmanager.dto

import com.codelectro.taskmanager.model.Priority
import java.time.LocalDateTime

data class TaskResponseDto(
        var id: Int?,
        var title: String,
        var description: String,
        var completed: Boolean = false,
        var priority: Priority,
        var createdAt: LocalDateTime = LocalDateTime.now(),
        var category: CategoryDto,
        var user: UserDto? = null
)