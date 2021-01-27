package com.codelectro.taskmanager.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class PasswordResetToken(
        @Id @GeneratedValue var id: Int? = null,
        @Column(nullable = false) var token: String,
        @Column(nullable = false) var expiresAt: LocalDateTime,
        @ManyToOne() @JoinColumn(name="user_id", nullable = false) var user: User
)