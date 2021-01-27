package com.codelectro.taskmanager.dto

data class NewPasswordRequest(
        val token: String,
        val password: String
)