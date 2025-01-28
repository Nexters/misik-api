package me.misik.api.domain

import jakarta.persistence.*
import me.misik.api.core.AggregateRoot


@Entity(name = "review")
@Table(name = "review")
@AggregateRoot
class Review(
    @Id
    @Column(name = "id")
    val id: Long,

    @Column(name = "is_completed")
    var isCompleted: Boolean,

    @Column(name = "text", length = 1000, columnDefinition = "VARCHAR(1000)", nullable = false)
    var text: String,

    @Column(name = "device_id", nullable = false, columnDefinition = "VARCHAR(100)")
    val deviceId: String,

    @Embedded
    val requestPrompt: RequestPrompt,
) : AbstractTime()
