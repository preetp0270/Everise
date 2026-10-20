package com.example.everise.core.common

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val iso = DateTimeFormatter.ISO_LOCAL_DATE

fun todayIsoDate(zone: ZoneId = ZoneId.systemDefault()): String =
    LocalDate.now(zone).format(iso)

fun LocalDate.toIso(): String = format(iso)
