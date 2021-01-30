package com.codelectro.taskmanager.service.task

import com.codelectro.taskmanager.dto.MessageResponse
import com.codelectro.taskmanager.dto.PagingResponseDto
import com.codelectro.taskmanager.dto.TaskRequestDto
import com.codelectro.taskmanager.dto.TaskResponseDto
import com.codelectro.taskmanager.model.Priority

interface TaskService {
    fun createTask(taskRequestDto: TaskRequestDto): TaskResponseDto
    fun getTasksWithPagination(
        completed: Boolean?,
        priority: Priority?,
        query: String?,
        page: Int,
        size: Int,
        email: String
    ): PagingResponseDto<TaskResponseDto>
    fun deleteTask(id: Int): MessageResponse
    fun updateTask(taskRequestDto: TaskRequestDto, id: Int): TaskResponseDto
}