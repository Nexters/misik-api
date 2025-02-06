package me.misik.api.domain.prompt

import jakarta.persistence.*
import me.misik.api.domain.AbstractTime
import me.misik.api.domain.ReviewStyle

@Entity
@Table(
    name = "prompt",
    uniqueConstraints = [UniqueConstraint(columnNames = ["style"])]
)
class Prompt(
    @Id
    @Column(name = "id")
    val id: Long,

    @Column(name = "style", columnDefinition = "VARCHAR(20)", nullable = false)
    @Enumerated(EnumType.STRING)
    val style: ReviewStyle,

    @Column(name = "command", columnDefinition = "TEXT", nullable = false)
    val command: String,
) : AbstractTime()
