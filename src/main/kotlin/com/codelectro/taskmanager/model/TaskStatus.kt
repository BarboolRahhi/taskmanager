package com.codelectro.taskmanager.model

interface TaskStatus {
    fun getProjectId(): Int
    fun getTotalTask(): Int
    fun getCountOfTodoTask(): Int
    fun getCountOfInProgressTask(): Int
    fun getCountOfDoneTask(): Int
}