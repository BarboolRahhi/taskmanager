package com.codelectro.taskmanager.model

interface TaskStatus {
    fun getProjectId(): Int
    fun getTotalTask(): Int
    fun getCountOfDoneTask(): Int
}