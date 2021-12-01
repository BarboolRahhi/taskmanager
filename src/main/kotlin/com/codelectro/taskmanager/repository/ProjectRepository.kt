package com.codelectro.taskmanager.repository

import com.codelectro.taskmanager.model.Project
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProjectRepository : JpaRepository<Project, Int> {
    @Query("select p from Project p join fetch p.user u where u.email = :email")
    fun findByUserEmail(email: String): List<Project>
}