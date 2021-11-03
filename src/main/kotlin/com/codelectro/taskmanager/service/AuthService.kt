package com.codelectro.taskmanager.service

import com.codelectro.taskmanager.dto.*
import com.codelectro.taskmanager.exception.AlreadyExistsException
import com.codelectro.taskmanager.model.PasswordResetToken
import com.codelectro.taskmanager.model.User
import com.codelectro.taskmanager.repository.ResetPasswordRepository
import com.codelectro.taskmanager.repository.UserRepository
import com.codelectro.taskmanager.security.JwtUtils
import com.codelectro.taskmanager.service.email.EmailSender
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class AuthService(
        private val authenticationManager: AuthenticationManager,
        private val userRepository: UserRepository,
        private val resetPasswordRepository: ResetPasswordRepository,
        private val passwordEncoder: PasswordEncoder,
        private val jwtUtils: JwtUtils,
        private val emailSender: EmailSender
) {

    @Transactional
    fun updatePassword(request: NewPasswordRequest): MessageResponse {
        val resetPasswordToken = resetPasswordRepository.findByToken(request.token)
                ?: throw RuntimeException("Invalid Token!")

        if (resetPasswordToken.expiresAt.isBefore(LocalDateTime.now())) {
            throw IllegalStateException("Token Expired!")
        }

        val user = resetPasswordToken.user.copy(password = passwordEncoder.encode(request.password))
        userRepository.save(user)
        resetPasswordRepository.delete(resetPasswordToken)
        return MessageResponse("Your new password updated successfully!")
    }

    @Transactional
    fun sendResetPasswordToken(request: PasswordResetRequest): MessageResponse {
        val user = userRepository.findByEmail(request.email!!)
                ?: throw UsernameNotFoundException("Email not Found!")
        val token = UUID.randomUUID().toString()
        saveResetPasswordToken(user, token)
        val link = "http://localhost:8082/api/auth/reset_password?token=$token"
        val sendEmail = """
               <h5>Hello,</h5>
                <p>You have requested to reset your password.</p>
                <p>Click the link below to change your password:</p>
                <a href="$link">Change my password</a>
                <br>
                <p>Ignore this email if you do remember your password, or you have not made the request.</p>
        """.trimIndent()
        emailSender.send(subject = "Reset your password!", user.email, sendEmail)
        return MessageResponse("We have sent a reset password link to your email. Please check your mail id.")
    }

    @Transactional
    fun saveResetPasswordToken(user: User, token: String) {
        val resetPasswordToken = PasswordResetToken(
                token = token,
                expiresAt = LocalDateTime.now().plusMinutes(5),
                user = user
        )
        resetPasswordRepository.save(resetPasswordToken)
    }

    @Transactional
    fun authenticatedUser(loginRequest: LoginRequest): JwtResponse {
        val authentication = authenticationManager
                .authenticate(UsernamePasswordAuthenticationToken(
                        loginRequest.email, loginRequest.password
                ))

        println("${authentication.name} - ${authentication.details} - ${authentication.principal}")
        SecurityContextHolder.getContext().authentication = authentication
        val token: String? = jwtUtils.generateJwtToken(authentication)
        return JwtResponse(token = token)
    }

    @Transactional
    fun registerUser(signupRequest: SignupRequest): MessageResponse {
        if (userRepository.existsByEmail(signupRequest.email)) {
            throw AlreadyExistsException("Email already exists!")
        }

        val newUser = signupRequest.toUser()
        userRepository.save(newUser)
        return MessageResponse("Registered Successfully!")
    }

    fun getCurrentLoggedUser(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.name
    }

    fun SignupRequest.toUser() = User(
            username = username,
            email = email,
            password = passwordEncoder.encode(password),
            imageURL = imageURL
    )


}