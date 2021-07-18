package com.codelectro.taskmanager.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity(name = "task_manager_user")
data class User(
        @Id @GeneratedValue var id: Int? = null,
        @Column(nullable = false)
        var username: String,
        @Column(nullable = false, unique = true)
        var email: String,
        @Column(nullable = false)
        var password: String,
        @Column(nullable = true)
        var imageURL: String?
)