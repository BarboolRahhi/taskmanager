package com.codelectro.taskmanager.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Task(
        @Id @GeneratedValue
        var id: Int? = null,
        @Column(length = 100, nullable = false)
        var title: String,
        @Column(nullable = false)
        var description: String,
        @Column(length = 20, nullable = false)
        @Enumerated(EnumType.STRING)
        var status: Status = Status.TODO,
        @Column(length = 20, nullable = false)
        @Enumerated(EnumType.STRING)
        var priority: Priority,
        @Column(nullable = false)
        var createdAt: LocalDateTime = LocalDateTime.now(),
        @Column(nullable = true)
        var completedAt: LocalDateTime? = null,
        @ManyToOne() @JoinColumn(name="project_id", nullable = false)
        var project: Project,
        @ManyToOne() @JoinColumn(name="user_id", nullable = false)
        var user: User
)