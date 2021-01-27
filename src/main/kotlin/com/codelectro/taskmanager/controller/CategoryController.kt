package com.codelectro.taskmanager.controller

import com.codelectro.taskmanager.dto.CategoryDto
import com.codelectro.taskmanager.service.category.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/category")
class CategoryController(
        private val categoryService: CategoryService
) {

    @PostMapping
    fun createCategory(@RequestBody categoryDto: CategoryDto) =
            ResponseEntity
                    .status(HttpStatus.OK)
                    .body(categoryService.createCategory(categoryDto))

    @GetMapping
    fun getAllCategories(authentication: Authentication) =
            ResponseEntity
                    .status(HttpStatus.OK)
                    .body(categoryService.getCategoriesByUser(authentication.name))

    @PutMapping("/{id}")
    fun updateCategory(
            @RequestBody categoryDto: CategoryDto,
            @PathVariable id: Int
    ) =
            ResponseEntity
                    .status(HttpStatus.OK)
                    .body(categoryService.updateCategory(categoryDto, id))


    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Int) =
            ResponseEntity
                    .status(HttpStatus.OK)
                    .body(categoryService.deleteCategory(id))
}