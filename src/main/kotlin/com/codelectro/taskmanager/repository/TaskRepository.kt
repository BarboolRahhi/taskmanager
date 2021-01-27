package com.codelectro.taskmanager.repository

import com.codelectro.taskmanager.model.Task
import org.springframework.data.jpa.repository.JpaRepository

interface TaskRepository : JpaRepository<Task, Int> {
    fun findByUserEmail(email: String): List<Task>
}