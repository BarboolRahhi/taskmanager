package com.codelectro.taskmanager.service.project

import com.codelectro.taskmanager.dto.MessageResponse
import com.codelectro.taskmanager.dto.project.ProjectRequest
import com.codelectro.taskmanager.dto.project.ProjectResponse

interface ProjectService {
    fun createProject(projectRequest: ProjectRequest): ProjectResponse
    fun getProjectsByUser(email: String): List<ProjectResponse>
    fun deleteProject(id: Int): MessageResponse
    fun updateProject(projectDto: ProjectRequest, id: Int): ProjectResponse
}