package me.misik.api.domain.prompt

import jakarta.persistence.*
import me.misik.api.domain.AbstractTime
import me.misik.api.domain.Style

@Entity
@Table(
    name = "prompt",
    uniqueConstraints = [UniqueConstraint(columnNames = ["style"])],
)
class Prompt(
    @Id
    @Column(name = "id")
    val id: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "style", columnDefinition = "VARCHAR(20)", nullable = false)
    val style: Style,

    @Column(name = "command", columnDefinition = "TEXT", nullable = false)
    val command: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "VARCHAR(20)", nullable = false)
    val type: PromptType,
) : AbstractTime()
