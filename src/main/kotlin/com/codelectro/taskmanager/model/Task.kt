package com.codelectro.taskmanager.model

import org.hibernate.Hibernate
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
        @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="project_id", nullable = false)
        var project: Project,
        @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="user_id", nullable = false)
        var user: User
) {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
                other as Task

                return id != null && id == other.id
        }

        override fun hashCode(): Int = 0

        @Override
        override fun toString(): String {
                return this::class.simpleName + "(id = $id )"
        }
}