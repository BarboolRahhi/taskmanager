package com.codelectro.taskmanager.model

import org.hibernate.Hibernate
import java.time.LocalDate
import jakarta.persistence.*

@Entity
data class Project(
        @Id @GeneratedValue
        var id: Int?,
        @Column(nullable = false)
        var name: String,
        @Column(nullable = false)
        var description: String,
        @Column(nullable = false)
        var startDate: LocalDate,
        @Column(nullable = true)
        var dueDate: LocalDate?,
        @Column(nullable = true)
        var isCompleted: Boolean?,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name="user_id", nullable = false)
        var user: User
) {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
                other as Project

                return id != null && id == other.id
        }

        override fun hashCode(): Int = 0

        @Override
        override fun toString(): String {
                return this::class.simpleName + "(id = $id )"
        }
}