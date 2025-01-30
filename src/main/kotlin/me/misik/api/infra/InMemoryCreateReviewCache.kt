package me.misik.api.infra

import me.misik.api.domain.CreateReviewCache
import me.misik.api.domain.Review
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemoryCreateReviewCache : CreateReviewCache {

    private val logger = LoggerFactory.getLogger(this::class.simpleName)
    private val cache = ConcurrentHashMap<Long, Review>()

    override fun get(id: Long): Review {
        logger.debug("cache get: "+ (cache.get(id)?.text ?:null ))
        return cache[id] ?: throw IllegalArgumentException("Review not found.")
    }

    override fun put(id: Long, review: Review) {
        logger.debug("cache put"+ (cache.get(id)?.text ?: null))
        cache[id] = review
    }

    override fun remove(id: Long) {
        logger.debug("cache remove: "+ (cache.get(id)?.text ?: null))
        cache.remove(id)
    }
}