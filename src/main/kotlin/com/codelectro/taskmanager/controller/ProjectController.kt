package com.codelectro.taskmanager.controller

import com.codelectro.taskmanager.dto.project.ProjectRequest
import com.codelectro.taskmanager.repository.TaskRepository
import com.codelectro.taskmanager.service.project.ProjectService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/project")
class ProjectController(
        private val projectService: ProjectService,
        private val taskRepository: TaskRepository
) {

    @PostMapping
    fun createCategory(@RequestBody projectDto: ProjectRequest) =
            ResponseEntity
                    .status(HttpStatus.OK)
                    .body(projectService.createProject(projectDto))

    @GetMapping
    fun getAllCategories(authentication: Authentication) =
            ResponseEntity
                    .status(HttpStatus.OK)
                    .body(projectService.getProjectsByUser(authentication.name))

    @GetMapping("/test")
    fun getAllCategories1(authentication: Authentication) =
        ResponseEntity
            .status(HttpStatus.OK)
            .body(taskRepository.getStatusOfTaskByUser(authentication.name))

    @PutMapping("/{id}")
    fun updateCategory(
        @RequestBody projectDto: ProjectRequest,
        @PathVariable id: Int
    ) =
            ResponseEntity
                    .status(HttpStatus.OK)
                    .body(projectService.updateProject(projectDto, id))


    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Int) =
            ResponseEntity
                    .status(HttpStatus.OK)
                    .body(projectService.deleteProject(id))
}