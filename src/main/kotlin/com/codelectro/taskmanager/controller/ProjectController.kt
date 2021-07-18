package com.codelectro.taskmanager.controller

import com.codelectro.taskmanager.dto.ProjectDto
import com.codelectro.taskmanager.service.project.ProjectService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/category")
class ProjectController(
        private val projectService: ProjectService
) {

    @PostMapping
    fun createCategory(@RequestBody projectDto: ProjectDto) =
            ResponseEntity
                    .status(HttpStatus.OK)
                    .body(projectService.createCategory(projectDto))

    @GetMapping
    fun getAllCategories(authentication: Authentication) =
            ResponseEntity
                    .status(HttpStatus.OK)
                    .body(projectService.getCategoriesByUser(authentication.name))

    @PutMapping("/{id}")
    fun updateCategory(
        @RequestBody projectDto: ProjectDto,
        @PathVariable id: Int
    ) =
            ResponseEntity
                    .status(HttpStatus.OK)
                    .body(projectService.updateCategory(projectDto, id))


    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Int) =
            ResponseEntity
                    .status(HttpStatus.OK)
                    .body(projectService.deleteCategory(id))
}