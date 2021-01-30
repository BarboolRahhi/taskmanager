package com.codelectro.taskmanager.service.task

import com.codelectro.taskmanager.dto.MessageResponse
import com.codelectro.taskmanager.dto.PagingResponseDto
import com.codelectro.taskmanager.dto.TaskRequestDto
import com.codelectro.taskmanager.dto.TaskResponseDto
import com.codelectro.taskmanager.exception.NotFoundException
import com.codelectro.taskmanager.exception.UnauthorizedException
import com.codelectro.taskmanager.model.Priority
import com.codelectro.taskmanager.model.Task
import com.codelectro.taskmanager.model.User
import com.codelectro.taskmanager.repository.CategoryRepository
import com.codelectro.taskmanager.repository.TaskRepository
import com.codelectro.taskmanager.repository.UserRepository
import com.codelectro.taskmanager.service.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root


@Service
class TaskServiceImpl(
    private val taskRepository: TaskRepository,
    private val categoryRepository: CategoryRepository,
    private val userRepository: UserRepository,
    private val authService: AuthService
) : TaskService {

    companion object {
        const val PRIORITY = "priority"
        const val COMPLETED = "completed"
        const val USER = "user"
        const val EMAIL = "email"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
    }

    override fun createTask(taskRequestDto: TaskRequestDto): TaskResponseDto {
        val currentUserEmail = authService.getCurrentLoggedUser()
        val user = userRepository.findByEmail(currentUserEmail)
            ?: throw NotFoundException("User Not Found!")

        val category = categoryRepository.findById(taskRequestDto.categoryId)
            .orElseThrow { throw RuntimeException("Category Not Found") }
        if (category.user.email != currentUserEmail) {
            throw UnauthorizedException("User Not allowed to Delete!")
        }

        return taskRepository.save(taskRequestDto.toTask(category, user))
            .toTaskResponseDto()
    }

    override fun getTasksWithPagination(
        completed: Boolean?,
        priority: Priority?,
        query: String?,
        page: Int,
        size: Int,
        email: String
    ): PagingResponseDto<TaskResponseDto> {
        val pageable = PageRequest.of(page - 1, size);
        return getPagingDto {
            taskRepository.findAll(getSpecification(completed, priority, query, email), pageable)
        }
    }

    fun getSpecification(
        completed: Boolean?,
        priority: Priority?,
        query: String?,
        email: String
    ): Specification<Task> {
        return Specification<Task> { root, cq, cb ->
            var predicate = cb.conjunction()
            predicate = cb.and(predicate, cb.equal(root.get<User>(USER).get<String>(EMAIL), email))

            query?.let {
                predicate =
                    cb.and(predicate, cb.like(cb.concat(root.get(TITLE), root.get(DESCRIPTION)), "%$query%"))
            }
            completed?.let {
                predicate = cb.and(predicate, cb.equal(root.get<Boolean>(COMPLETED), completed))
            }
            priority?.let {
                predicate = cb.and(predicate, cb.equal(root.get<Priority>(PRIORITY), priority))
            }
            cq.orderBy(cb.desc(root.get<LocalDateTime>("createdAt")))
            return@Specification predicate
        }
    }

    private fun getPagingDto(pageTask: () -> Page<Task>): PagingResponseDto<TaskResponseDto> {
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