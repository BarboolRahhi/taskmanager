package com.codelectro.taskmanager.service.project

import com.codelectro.taskmanager.dto.MessageResponse
import com.codelectro.taskmanager.dto.project.ProjectRequest
import com.codelectro.taskmanager.dto.project.ProjectResponse
import com.codelectro.taskmanager.exception.NotFoundException
import com.codelectro.taskmanager.exception.UnauthorizedException
import com.codelectro.taskmanager.repository.ProjectRepository
import com.codelectro.taskmanager.repository.TaskRepository
import com.codelectro.taskmanager.repository.UserRepository
import com.codelectro.taskmanager.service.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProjectServiceImpl(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
    private val authService: AuthService
) : ProjectService {

    @Transactional
    override fun createProject(projectRequest: ProjectRequest): ProjectResponse {
        val currentUserEmail = authService.getCurrentLoggedUser()

        val user = userRepository.findByEmail(currentUserEmail)
                ?: throw NotFoundException("User Not Found!")
        val category = projectRepository.save(projectRequest.toProjectEntity(user))

        return category.toProjectResponse(null)
    }

    @Transactional(readOnly = true)
    override fun getProjectsByUser(email: String): List<ProjectResponse> {
        val statusData =
            taskRepository.getStatusOfTaskByUser(email).map { taskStatus -> taskStatus.getProjectId() to taskStatus }.toMap()
        return projectRepository.findByUserEmail(email)
                .map { project -> project.toProjectResponse(statusData[project.id]) }
    }


    @Transactional
    override fun deleteProject(id: Int): MessageResponse {
        val project = projectRepository.findById(id)
                .orElseThrow {  throw NotFoundException("Category Not Found") }
        val currentUserEmail = authService.getCurrentLoggedUser()

        if (project.user.email != currentUserEmail) {
            throw UnauthorizedException("User Not allowed to Delete!")
        }
        taskRepository.deleteByProjectId(project.id!!)
        projectRepository.delete(project)
        return MessageResponse("Project Deleted!")
    }

    @Transactional
    override fun updateProject(projectDto: ProjectRequest, id: Int): ProjectResponse {

        val project = projectRepository.findById(id)
                .orElseThrow {  throw NotFoundException("Category Not Found") }

        val currentUserEmail = authService.getCurrentLoggedUser()

        if (project.user.email != currentUserEmail) {
            throw UnauthorizedException("User Not allowed to Update!")
        }

        return projectRepository.save(project.toUpdate(projectDto)).toProjectResponse(null)
    }

}