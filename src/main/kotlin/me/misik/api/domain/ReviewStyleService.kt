package me.misik.api.domain

import org.springframework.stereotype.Service

@Service
class ReviewStyleService {
    fun getAll(): Array<ReviewStyle> {
        return ReviewStyle.values()
    }
}