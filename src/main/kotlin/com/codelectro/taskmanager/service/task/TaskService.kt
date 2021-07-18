package com.codelectro.taskmanager.service.task

import com.codelectro.taskmanager.dto.MessageResponse
import com.codelectro.taskmanager.dto.PagingResponseDto
import com.codelectro.taskmanager.dto.TaskRequest
import com.codelectro.taskmanager.dto.TaskResponse
import com.codelectro.taskmanager.model.Priority
import com.codelectro.taskmanager.model.Status

interface TaskService {
    fun createTask(taskRequestDto: TaskRequest): TaskResponse
    fun getTasksWithPagination(
        projectId: Int?,
        status: Status?,
        priority: Priority?,
        query: String?,
        page: Int,
        size: Int,
        email: String
    ): PagingResponseDto<TaskResponse>
    fun deleteTask(id: Int): MessageResponse
    fun updateTask(taskRequestDto: TaskRequest, id: Int): TaskResponse
}