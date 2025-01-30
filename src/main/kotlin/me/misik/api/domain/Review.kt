package me.misik.api.domain

import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
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
    var isCompleted: Boolean,

    @Column(name = "text", length = 1000, columnDefinition = "VARCHAR(1000)", nullable = false)
    var text: String,

    @Column(name = "device_id", nullable = false, columnDefinition = "VARCHAR(100)")
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

    fun copy(
        id: Long = this.id,
        isCompleted: Boolean = this.isCompleted,
        text: String = this.text,
        deviceId: String = this.deviceId,
        requestPrompt: RequestPrompt = this.requestPrompt,
    ): Review {
        return Review(
            id = id,
            isCompleted = isCompleted,
            text = text,
            deviceId = deviceId,
            requestPrompt = requestPrompt,
        )
    }

    fun addText(newText: String) {
        text += newText
    }
}
