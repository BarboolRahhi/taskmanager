package com.codelectro.taskmanager.dto

data class SignupRequest(
        var username: String,
        var email: String,
        var password: String,
        var imageURL: String?
)