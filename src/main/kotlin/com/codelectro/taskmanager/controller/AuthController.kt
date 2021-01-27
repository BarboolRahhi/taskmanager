package com.codelectro.taskmanager.controller

import com.codelectro.taskmanager.dto.LoginRequest
import com.codelectro.taskmanager.dto.NewPasswordRequest
import com.codelectro.taskmanager.dto.PasswordResetRequest
import com.codelectro.taskmanager.dto.SignupRequest
import com.codelectro.taskmanager.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth/")
class AuthController(
        private val authService: AuthService
) {

    @PostMapping("login")
    fun authenticateUser(@RequestBody loginRequest: LoginRequest) =
            ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authService.authenticatedUser(loginRequest))

    @PostMapping("signup")
    fun registerUser(@RequestBody signupRequest: SignupRequest) =
            ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authService.registerUser(signupRequest))


    @PostMapping("forget_password")
    fun forgetPassword(@RequestBody resetPasswordRequest: PasswordResetRequest) =
            ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authService.sendResetPasswordToken(resetPasswordRequest))

    @PostMapping("reset_password")
    fun updatePassword(@RequestBody newPasswordRequest: NewPasswordRequest) =
            ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authService.updatePassword(newPasswordRequest))

}