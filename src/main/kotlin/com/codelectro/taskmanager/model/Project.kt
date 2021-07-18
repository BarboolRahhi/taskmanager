package com.codelectro.taskmanager.model

import javax.persistence.*

@Entity
data class Project(
        @Id @GeneratedValue var id: Int?,
        @Column(nullable = false)
        var name: String,
        @Column(nullable = false)
        var description: String,
        @ManyToOne()
        @JoinColumn(name="user_id", nullable = false)
        var user: User
)