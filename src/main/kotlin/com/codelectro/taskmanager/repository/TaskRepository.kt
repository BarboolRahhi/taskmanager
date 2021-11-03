package com.codelectro.taskmanager.repository

import com.codelectro.taskmanager.model.Task
import com.codelectro.taskmanager.model.TaskStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface TaskRepository : JpaRepository<Task, Int>, JpaSpecificationExecutor<Task> {
    fun findByUserEmail(email: String, pageable: Pageable): Page<Task>

    @Query(
        """
       select t.project.id as projectId, 
       count(t.project.id) as totalTask, 
       (select count(*) from Task t1 where t1.status = 'DONE' and t1.project.id = t.project.id) as countOfDoneTask 
       from Task t group by t.project.id
    """
    )
    fun getStatusOfTaskByUser(@Param("email") email: String): List<TaskStatus>

    @Modifying
    @Query("delete from Task t where t.project.id = :projectId")
    fun deleteByProjectId(@Param("projectId") projectId: Int)

}