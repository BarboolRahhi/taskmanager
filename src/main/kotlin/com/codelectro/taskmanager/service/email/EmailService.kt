package com.codelectro.taskmanager.service.email

import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import javax.mail.MessagingException

@Service
class EmailService(
        private val javaMailSender: JavaMailSender
): EmailSender {

    private val LOGGER = LoggerFactory
            .getLogger(EmailService::class.java)

    @Async
    override fun send(subject: String, to: String, email: String) {

        try {
            val mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper(mimeMessage, "utf-8").apply {
                setText(email, true)
                setTo(to)
                setSubject(subject)
                setFrom("barboolprojects@gmail.com")
            }
            javaMailSender.send(mimeMessage)
        } catch (e: MessagingException) {
            LOGGER.error("failed to send email", e)
            throw IllegalStateException("failed to send email")
        }
    }
}