package com.codelectro.taskmanager.service.project

import com.codelectro.taskmanager.dto.ProjectDto
import com.codelectro.taskmanager.dto.MessageResponse
import com.codelectro.taskmanager.exception.NotFoundException
import com.codelectro.taskmanager.exception.UnauthorizedException
import com.codelectro.taskmanager.repository.ProjectRepository
import com.codelectro.taskmanager.repository.UserRepository
import com.codelectro.taskmanager.service.AuthService
import com.codelectro.taskmanager.service.toProject
import com.codelectro.taskmanager.service.toProjectDto
import com.codelectro.taskmanager.service.toUpdate
import org.springframework.stereotype.Service

@Service
class ProjectServiceImpl(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    private val authService: AuthService
) : ProjectService {

    override fun createCategory(projectDto: ProjectDto): ProjectDto {
        val currentUserEmail = authService.getCurrentLoggedUser()

        val user = userRepository.findByEmail(currentUserEmail)
                ?: throw NotFoundException("User Not Found!")
        val category = projectRepository.save(projectDto.toProject(user))
        return category.toProjectDto()
    }

    override fun getCategoriesByUser(email: String): List<ProjectDto> {
        return projectRepository.findByUserEmail(email)
                .map { category -> category.toProjectDto() }
    }

    override fun deleteCategory(id: Int): MessageResponse {
        val category = projectRepository.findById(id)
                .orElseThrow {  throw NotFoundException("Category Not Found") }
        val currentUserEmail = authService.getCurrentLoggedUser()

        if (category.user.email != currentUserEmail) {
            throw UnauthorizedException("User Not allowed to Delete!")
        }
        projectRepository.delete(category)
        return MessageResponse("Category Deleted!")
    }

    override fun updateCategory(projectDto: ProjectDto, id: Int): ProjectDto {

        val category = projectRepository.findById(id)
                .orElseThrow {  throw NotFoundException("Category Not Found") }

        val currentUserEmail = authService.getCurrentLoggedUser()

        if (category.user.email != currentUserEmail) {
            throw UnauthorizedException("User Not allowed to Update!")
        }
        return projectRepository.save(category.toUpdate(projectDto)).toProjectDto();
    }

}