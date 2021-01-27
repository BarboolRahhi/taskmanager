package com.codelectro.taskmanager.service.task

import com.codelectro.taskmanager.dto.MessageResponse
import com.codelectro.taskmanager.dto.TaskRequestDto
import com.codelectro.taskmanager.dto.TaskResponseDto

interface TaskService {
    fun createTask(taskRequestDto: TaskRequestDto): TaskResponseDto
    fun getTasksByUser(email: String): List<TaskResponseDto>
    fun deleteTask(id: Int): MessageResponse
    fun updateTask(taskRequestDto: TaskRequestDto, id: Int): TaskResponseDto
}