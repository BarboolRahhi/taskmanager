package com.codelectro.taskmanager.service.category

import com.codelectro.taskmanager.dto.CategoryDto
import com.codelectro.taskmanager.dto.MessageResponse

interface CategoryService {
    fun createCategory(categoryDto: CategoryDto): CategoryDto
    fun getCategoriesByUser(email: String): List<CategoryDto>
    fun deleteCategory(id: Int): MessageResponse
    fun updateCategory(categoryDto: CategoryDto, id: Int): CategoryDto
}