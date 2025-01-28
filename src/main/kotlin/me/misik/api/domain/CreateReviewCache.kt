package me.misik.api.domain

interface CreateReviewCache {

    fun get(id: Long) : Review
    fun put(id: Long, review: Review)
    fun remove(id: Long)
}