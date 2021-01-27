package com.codelectro.taskmanager.dto

data class UserDto(
        var id: Int? = null,
        var username: String,
        var email: String,
        var imageURL: String?
)