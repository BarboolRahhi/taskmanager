package com.codelectro.taskmanager.repository

import com.codelectro.taskmanager.model.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Int> {
    fun findByUserEmail(email: String): List<Category>
}