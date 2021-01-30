package com.codelectro.taskmanager.dto

data class PagingResponseDto<T>(
    val data: List<T> = emptyList(),
    val totalItems: Long,
    val totalPages: Int,
    val currentPage: Int
)