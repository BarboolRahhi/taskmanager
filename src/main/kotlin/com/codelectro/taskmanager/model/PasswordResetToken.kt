package com.codelectro.taskmanager.model

import org.hibernate.Hibernate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class PasswordResetToken(
        @Id @GeneratedValue var id: Int? = null,
        @Column(nullable = false) var token: String,
        @Column(nullable = false) var expiresAt: LocalDateTime,
        @ManyToOne() @JoinColumn(name="user_id", nullable = false) var user: User
) {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
                other as PasswordResetToken

                return id != null && id == other.id
        }

        override fun hashCode(): Int = javaClass.hashCode()

        @Override
        override fun toString(): String {
                return this::class.simpleName + "(id = $id )"
        }
}