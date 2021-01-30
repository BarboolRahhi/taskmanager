package com.codelectro.taskmanager.repository

import com.codelectro.taskmanager.model.Priority
import com.codelectro.taskmanager.model.Task
import com.codelectro.taskmanager.model.User
import com.codelectro.taskmanager.service.task.TaskServiceImpl
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.time.LocalDateTime


interface TaskRepository : JpaRepository<Task, Int>, JpaSpecificationExecutor<Task> {
    fun findByUserEmail(email: String, pageable: Pageable): Page<Task>
}