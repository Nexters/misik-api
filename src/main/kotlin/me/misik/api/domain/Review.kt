package me.misik.api.domain

import jakarta.persistence.*
import me.misik.api.core.AggregateRoot
import me.misik.api.core.IdGenerator


@Entity(name = "review")
@Table(name = "review")
@AggregateRoot
class Review(
    @Id
    @Column(name = "id")
    val id: Long,

    @Column(name = "is_completed")
    val isCompleted: Boolean,

    @Column(name = "text", length = 300, columnDefinition = "VARCHAR(300)", nullable = false)
    val text: String,

    @Column(name = "device_id", columnDefinition = "VARCHAR(100)", nullable = false)
    val deviceId: String,

    @Embedded
    val requestPrompt: RequestPrompt,
) : AbstractTime() {

    companion object {

        fun create(
            text: String = "",
            deviceId: String,
            requestPrompt: RequestPrompt,
            isCompleted: Boolean = false,
        ): Review {

            return Review(
                id = IdGenerator.generate(),
                isCompleted = isCompleted,
                text = text,
                deviceId = deviceId,
                requestPrompt = requestPrompt,
            )
        }
    }
}
