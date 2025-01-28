package me.misik.api.domain

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ReviewRepository : JpaRepository<Review, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update review as r set r.text = concat(r.text, :text) where r.id = :id")
    fun addText(@Param("id") id: Long, @Param("text") text: String)

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update review as r set r.isCompleted = :completedStatus where r.id = :id")
    fun setReviewCompletedStatus(@Param("id") id: Long, @Param("completedStatus") completedStatus: Boolean)

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select r from review as r where r.id = :id")
    fun getReviewWithReadLock(@Param("id") id: Long): Review

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update review as r set r.text = :text, r.isCompleted = true where r.id = :id")
    fun updateTextAndComplete(id: Long, text: String): Any
}
