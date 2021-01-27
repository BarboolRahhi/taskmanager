package com.codelectro.taskmanager.dto

data class JwtResponse(
        val token: String?,
        val type: String = "Bearer"
)