package com.codelectro.taskmanager.controller

import com.codelectro.taskmanager.dto.TaskRequestDto
import com.codelectro.taskmanager.service.task.TaskService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/task")
class TaskController(
        private val taskService: TaskService
) {

    @PostMapping
    fun createTask(@RequestBody taskRequestDto: TaskRequestDto) =
            ResponseEntity
                    .status(HttpStatus.OK)
                    .body(taskService.createTask(taskRequestDto))

    @GetMapping
    fun getAllTasks(authentication: Authentication) =
            ResponseEntity
                    .status(HttpStatus.OK)
                    .body(taskService.getTasksByUser(authentication.name))

    @PutMapping("/{id}")
    fun updateTask(
            @RequestBody taskRequestDto: TaskRequestDto,
            @PathVariable id: Int
    ) =
            ResponseEntity
                    .status(HttpStatus.OK)
                    .body(taskService.updateTask(taskRequestDto, id))


    @DeleteMapping("/{id}")
    fun deleteTask(@RequestBody taskRequestDto: TaskRequestDto, @PathVariable id: Int) =
            ResponseEntity
                    .status(HttpStatus.OK)
                    .body(taskService.deleteTask(id))
}