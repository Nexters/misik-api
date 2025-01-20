package me.misik.api.domain.query

import jakarta.persistence.*
import me.misik.api.domain.AbstractTime
import me.misik.api.domain.ReviewStyle

@Entity
class Prompt(
    @Id
    @Column(name = "id")
    val id: Long,

    @Column(name = "style", columnDefinition = "VARCHAR(20)", nullable = false)
    @Enumerated(EnumType.STRING)
    val style: ReviewStyle,

    @Column(name = "text", columnDefinition = "TEXT", nullable = false)
    val text: String,
) : AbstractTime()
