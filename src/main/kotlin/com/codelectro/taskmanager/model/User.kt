package com.codelectro.taskmanager.model

import org.hibernate.Hibernate
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity(name = "task_manager_user")
data class User(
        @Id @GeneratedValue var id: Int? = null,
        @Column(nullable = false)
        var username: String,
        @Column(nullable = false, unique = true)
        var email: String,
        @Column(nullable = false)
        var password: String,
        @Column(nullable = true)
        var imageURL: String?
) {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
                other as User

                return id != null && id == other.id
        }

        override fun hashCode(): Int = javaClass.hashCode()

        @Override
        override fun toString(): String {
                return this::class.simpleName + "(id = $id )"
        }
}