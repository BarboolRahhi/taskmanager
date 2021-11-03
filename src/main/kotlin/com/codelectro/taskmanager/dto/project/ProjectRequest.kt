package com.codelectro.taskmanager.dto.project

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class ProjectRequest(
    var name: String,
    var description: String,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    var startDate: LocalDate,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    var dueDate: LocalDate?,
    var isCompleted: Boolean? = false
)
