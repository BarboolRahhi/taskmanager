package com.codelectro.taskmanager.service.email

interface EmailSender {
    fun send(subject: String, to: String, email: String)
}