package com.codelectro.taskmanager.service.task

import com.codelectro.taskmanager.dto.MessageResponse
import com.codelectro.taskmanager.dto.TaskRequestDto
import com.codelectro.taskmanager.dto.TaskResponseDto
import com.codelectro.taskmanager.exception.NotFoundException
import com.codelectro.taskmanager.exception.UnauthorizedException
import com.codelectro.taskmanager.model.Category
import com.codelectro.taskmanager.model.Task
import com.codelectro.taskmanager.model.User
import com.codelectro.taskmanager.repository.CategoryRepository
import com.codelectro.taskmanager.repository.TaskRepository
import com.codelectro.taskmanager.repository.UserRepository
import com.codelectro.taskmanager.service.*
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class TaskServiceImpl(
        private val taskRepository: TaskRepository,
        private val categoryRepository: CategoryRepository,
        private val userRepository: UserRepository,
        private val authService: AuthService
) : TaskService {

    override fun createTask(taskRequestDto: TaskRequestDto): TaskResponseDto {
        val currentUserEmail = authService.getCurrentLoggedUser()
        val user = userRepository.findByEmail(currentUserEmail)
                ?: throw NotFoundException("User Not Found!")

        val category = categoryRepository.findById(taskRequestDto.categoryId)
                .orElseThrow {  throw RuntimeException("Category Not Found") }
        if (category.user.email != currentUserEmail) {
            throw UnauthorizedException("User Not allowed to Delete!")
        }

        return taskRepository.save(taskRequestDto.toTask(category, user))
                .toTaskResponseDto()
    }

    override fun getTasksByUser(email: String): List<TaskResponseDto> {
        return taskRepository.findByUserEmail(email)
                .map { task -> task.toTaskResponseDto() }
    }

    override fun deleteTask(id: Int): MessageResponse {
       val task = taskRepository.findById(id)
               .orElseThrow { throw NotFoundException("Task not Found!") }

        val currentUserEmail = authService.getCurrentLoggedUser()
        if (task.user.email != currentUserEmail) {
            throw UnauthorizedException("User Not allowed to Delete!")
        }
        taskRepository.delete(task)
        return MessageResponse("Task Deleted!")
    }

    override fun updateTask(taskRequestDto: TaskRequestDto, id: Int): TaskResponseDto {
        val task = taskRepository.findById(id)
                .orElseThrow { throw NotFoundException("Task not Found!") }

        val currentUserEmail = authService.getCurrentLoggedUser()

        if (task.user.email != currentUserEmail) {
            throw UnauthorizedException("User Not allowed to Delete!")
        }

        return taskRepository.save(task.toUpdate(taskRequestDto))
                .toTaskResponseDto()
    }



}