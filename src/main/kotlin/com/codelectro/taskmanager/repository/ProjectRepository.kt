package com.codelectro.taskmanager.repository

import com.codelectro.taskmanager.model.Project
import org.springframework.data.jpa.repository.JpaRepository

interface ProjectRepository : JpaRepository<Project, Int> {
    fun findByUserEmail(email: String): List<Project>
}