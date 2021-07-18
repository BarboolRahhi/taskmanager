package com.codelectro.taskmanager.service.project

import com.codelectro.taskmanager.dto.ProjectDto
import com.codelectro.taskmanager.dto.MessageResponse

interface ProjectService {
    fun createCategory(projectDto: ProjectDto): ProjectDto
    fun getCategoriesByUser(email: String): List<ProjectDto>
    fun deleteCategory(id: Int): MessageResponse
    fun updateCategory(projectDto: ProjectDto, id: Int): ProjectDto
}