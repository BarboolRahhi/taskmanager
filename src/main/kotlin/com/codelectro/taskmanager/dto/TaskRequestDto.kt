package com.codelectro.taskmanager.dto

import com.codelectro.taskmanager.model.Priority

data class TaskRequestDto(
        var title: String,
        var description: String,
        var completed: Boolean = false,
        var priority: Priority,
        var categoryId: Int
)