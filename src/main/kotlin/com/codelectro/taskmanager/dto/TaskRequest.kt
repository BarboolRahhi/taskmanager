package com.codelectro.taskmanager.dto

import com.codelectro.taskmanager.model.Priority
import com.codelectro.taskmanager.model.Status

data class TaskRequest(
        var title: String,
        var description: String,
        var status: Status?,
        var priority: Priority,
        var categoryId: Int
)