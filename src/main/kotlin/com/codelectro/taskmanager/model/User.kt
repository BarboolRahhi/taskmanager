package com.codelectro.taskmanager.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity(name = "task_manager_user")
data class User(
        @Id @GeneratedValue var id: Int? = null,
        var username: String,
        var email: String,
        var password: String,
        var imageURL: String?
)