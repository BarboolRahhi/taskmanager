package com.codelectro.taskmanager.service

import com.codelectro.taskmanager.dto.CategoryDto
import com.codelectro.taskmanager.dto.TaskRequestDto
import com.codelectro.taskmanager.dto.TaskResponseDto
import com.codelectro.taskmanager.dto.UserDto
import com.codelectro.taskmanager.model.Category
import com.codelectro.taskmanager.model.Task
import com.codelectro.taskmanager.model.User

fun User.toUserDto() = UserDto(
        id = id,
        username = username,
        email = email,
        imageURL = imageURL
)
fun CategoryDto.toCategory(user: User) = Category(id, name, description, user = user)

fun Category.toCategoryDto() = CategoryDto(id, name, description)

fun Category.toUpdate(categoryDto: CategoryDto) = this.copy(
        name = categoryDto.name,
        description = categoryDto.description,
)

fun TaskRequestDto.toTask(category: Category, user: User) = Task(
        title = title,
        description = description,
        completed = completed,
        priority = priority,
        category = category,
        user = user
)

fun Task.toTaskResponseDto() = TaskResponseDto(
        id, title, description, completed, priority, createdAt, category.toCategoryDto(), user.toUserDto()
)

fun Task.toUpdate(dto: TaskRequestDto) = this.copy(
        title = dto.title,
        description = dto.description,
        completed = dto.completed,
        priority = dto.priority,
)