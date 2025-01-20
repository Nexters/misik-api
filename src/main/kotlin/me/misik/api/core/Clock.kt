package me.misik.api.core

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

object Clock {

    var clock: Clock = Clock.systemUTC()

    fun instant() = Instant.now(clock)

    fun Instant.toZonedDateTime() = ZonedDateTime.ofInstant(this, clock.zone)

    fun Instant.toZonedDateTime(zoneId: ZoneId) = ZonedDateTime.ofInstant(this, zoneId)

    fun Instant.toKr() = ZonedDateTime.ofInstant(this, ZoneId.of("Asia/Seoul"))

}
