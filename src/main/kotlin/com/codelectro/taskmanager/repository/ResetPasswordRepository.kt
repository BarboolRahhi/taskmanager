package com.codelectro.taskmanager.repository

import com.codelectro.taskmanager.model.PasswordResetToken
import org.springframework.data.jpa.repository.JpaRepository

interface ResetPasswordRepository : JpaRepository<PasswordResetToken, Int> {
    fun findByToken(token: String): PasswordResetToken?
}