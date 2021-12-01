package com.codelectro.taskmanager.dto.project

data class TaskDetails(
    var totalTask: Int,
    var progress: Int,
    var countOfTodo: Int,
    var countOfInProgress: Int,
    var countOfDone: Int
)