package com.codelectro.taskmanager.controller

import com.codelectro.taskmanager.dto.TaskRequest
import com.codelectro.taskmanager.model.Priority
import com.codelectro.taskmanager.model.Status
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
    fun createTask(@RequestBody taskRequestDto: TaskRequest) =
        ResponseEntity
            .status(HttpStatus.OK)
            .body(taskService.createTask(taskRequestDto))

    @GetMapping()
    fun getTasksWithPagination(
        @RequestParam(required = false) projectId: Int?,
        @RequestParam(required = false) status: Status?,
        @RequestParam(required = false) priority: Priority?,
        @RequestParam(required = false) query: String?,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "2") size: Int,
        authentication: Authentication
    ) = ResponseEntity
        .status(HttpStatus.OK)
        .body(
            taskService
                .getTasksWithPagination(projectId, status, priority, query, page, size, authentication.name)
        )

    @PutMapping("/{id}")
    fun updateTask(
        @RequestBody taskRequestDto: TaskRequest,
        @PathVariable id: Int
    ) = ResponseEntity
        .status(HttpStatus.OK)
        .body(taskService.updateTask(taskRequestDto, id))


    @DeleteMapping("/{id}")
    fun deleteTask(@RequestBody taskRequestDto: TaskRequest, @PathVariable id: Int) =
        ResponseEntity
            .status(HttpStatus.OK)
            .body(taskService.deleteTask(id))
}