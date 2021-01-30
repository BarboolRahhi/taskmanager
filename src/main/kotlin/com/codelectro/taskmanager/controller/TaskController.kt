package com.codelectro.taskmanager.controller

import com.codelectro.taskmanager.dto.TaskRequestDto
import com.codelectro.taskmanager.model.Priority
import com.codelectro.taskmanager.model.Task
import com.codelectro.taskmanager.repository.TaskRepository
import com.codelectro.taskmanager.service.task.TaskService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tasks")
class TaskController(
    private val taskService: TaskService,
    private val taskRepository: TaskRepository
) {

    @PostMapping
    fun createTask(@RequestBody taskRequestDto: TaskRequestDto) =
        ResponseEntity
            .status(HttpStatus.OK)
            .body(taskService.createTask(taskRequestDto))

    @GetMapping()
    fun getTasksWithPagination(
        @RequestParam(required = false) completed: Boolean?,
        @RequestParam(required = false) priority: Priority?,
        @RequestParam(required = false) query: String?,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "2") size: Int,
        authentication: Authentication
    ) = ResponseEntity
        .status(HttpStatus.OK)
        .body(
            taskService
                .getTasksWithPagination(completed, priority, query, page, size, authentication.name)
        )


    @PutMapping("/{id}")
    fun updateTask(
        @RequestBody taskRequestDto: TaskRequestDto,
        @PathVariable id: Int
    ) = ResponseEntity
        .status(HttpStatus.OK)
        .body(taskService.updateTask(taskRequestDto, id))


    @DeleteMapping("/{id}")
    fun deleteTask(@RequestBody taskRequestDto: TaskRequestDto, @PathVariable id: Int) =
        ResponseEntity
            .status(HttpStatus.OK)
            .body(taskService.deleteTask(id))
}