package com.codelectro.taskmanager.model

import javax.persistence.*

@Entity
data class Category(
        @Id @GeneratedValue var id: Int?,
        var name: String,
        var description: String,
        @ManyToOne()
        @JoinColumn(name="user_id")
        var user: User
)