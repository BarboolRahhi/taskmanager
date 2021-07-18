package com.codelectro.taskmanager.service.task

import com.codelectro.taskmanager.dto.MessageResponse
import com.codelectro.taskmanager.dto.PagingResponseDto
import com.codelectro.taskmanager.dto.TaskRequest
import com.codelectro.taskmanager.dto.TaskResponse
import com.codelectro.taskmanager.exception.NotFoundException
import com.codelectro.taskmanager.exception.UnauthorizedException
import com.codelectro.taskmanager.model.*
import com.codelectro.taskmanager.repository.ProjectRepository
import com.codelectro.taskmanager.repository.TaskRepository
import com.codelectro.taskmanager.repository.UserRepository
import com.codelectro.taskmanager.service.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class TaskServiceImpl(
    private val taskRepository: TaskRepository,
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    private val authService: AuthService
) : TaskService {

    companion object {
        const val PRIORITY = "priority"
        const val STATUS = "status"
        const val USER = "user"
        const val EMAIL = "email"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val PROJECT = "project"
        const val ID = "id"
    }

    override fun createTask(taskRequestDto: TaskRequest): TaskResponse {
        val currentUserEmail = authService.getCurrentLoggedUser()
        val user = userRepository.findByEmail(currentUserEmail)
            ?: throw NotFoundException("User Not Found!")

        val category = projectRepository.findById(taskRequestDto.categoryId)
            .orElseThrow { throw RuntimeException("Category Not Found") }
        if (category.user.email != currentUserEmail) {
            throw UnauthorizedException("Access is denied due to invalid credentials!")
        }

        return taskRepository.save(taskRequestDto.toTask(category, user))
            .toTaskResponseDto()
    }

    override fun getTasksWithPagination(
        projectId: Int?,
        status: Status?,
        priority: Priority?,
        query: String?,
        page: Int,
        size: Int,
        email: String
    ): PagingResponseDto<TaskResponse> {
        val pageable = PageRequest.of(page - 1, size);
        return getPagingDto {
            taskRepository.findAll(getSpecification(projectId, status, priority, query, email), pageable)
        }
    }

    fun getSpecification(
        projectId: Int?,
        status: Status?,
        priority: Priority?,
        query: String?,
        email: String
    ): Specification<Task> {
        return Specification<Task> { root, cq, cb ->
            var predicate = cb.conjunction()
            predicate = cb.and(predicate, cb.equal(root.get<User>(USER).get<String>(EMAIL), email))

            projectId?.let {
                predicate = cb.and(predicate, cb.equal(root.get<Project>(PROJECT).get<Int>(ID), projectId))
            }

            query?.let {
                predicate =
                    cb.and(predicate, cb.like(cb.concat(root.get(TITLE), root.get(DESCRIPTION)), "%$query%"))
            }
            status?.let {
                predicate = cb.and(predicate, cb.equal(root.get<Status>(STATUS), status))
            }
            priority?.let {
                predicate = cb.and(predicate, cb.equal(root.get<Priority>(PRIORITY), priority))
            }
            cq.orderBy(cb.desc(root.get<LocalDateTime>("createdAt")))
            return@Specification predicate
        }
    }

    private fun getPagingDto(pageTask: () -> Page<Task>): PagingResponseDto<TaskResponse> {
        val taskDto = pageTask().content.map { task -> task.toTaskResponseDto() }
        return PagingResponseDto(
            data = taskDto,
            totalItems = pageTask().totalElements,
            totalPages = pageTask().totalPages,
            currentPage = pageTask().number + 1
        )
    }


    override fun deleteTask(id: Int): MessageResponse {
        val task = taskRepository.findById(id)
            .orElseThrow { throw NotFoundException("Task not Found!") }

        val currentUserEmail = authService.getCurrentLoggedUser()
        if (task.user.email != currentUserEmail) {
            throw UnauthorizedException("Access is denied due to invalid credentials")
        }
        taskRepository.delete(task)
        return MessageResponse("Task Deleted!")
    }

    override fun updateTask(taskRequestDto: TaskRequest, id: Int): TaskResponse {
        val task = taskRepository.findById(id)
            .orElseThrow { throw NotFoundException("Task not Found!") }

        val currentUserEmail = authService.getCurrentLoggedUser()

        if (task.user.email != currentUserEmail) {
            throw UnauthorizedException("Access is denied due to invalid credentials")
        }

        return taskRepository.save(task.toUpdate(taskRequestDto))
            .toTaskResponseDto()
    }


}