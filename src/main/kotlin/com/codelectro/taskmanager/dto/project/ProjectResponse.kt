package com.codelectro.taskmanager.dto.project

import java.time.LocalDate

data class ProjectResponse(
    var id: Int?,
    var name: String,
    var description: String,
    var startDate: LocalDate,
    var dueDate: LocalDate?,
    var isCompleted: Boolean?,
    var taskDetails: TaskDetails
)

