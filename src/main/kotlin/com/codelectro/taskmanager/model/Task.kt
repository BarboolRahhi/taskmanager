package com.codelectro.taskmanager.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Task(
        @Id @GeneratedValue var id: Int? = null,
        var title: String,
        var description: String,
        var completed: Boolean,
        @Enumerated(EnumType.STRING) var priority: Priority,
        var createdAt: LocalDateTime = LocalDateTime.now(),
        @ManyToOne() @JoinColumn(name="category_id") var category: Category,
        @ManyToOne() @JoinColumn(name="user_id") var user: User
)