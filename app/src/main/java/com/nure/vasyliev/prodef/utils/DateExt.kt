package com.nure.vasyliev.prodef.utils

import java.text.SimpleDateFormat
import java.util.Locale

const val SECONDS_IN_MINUTE = 60L
const val MILLIS_IN_SECOND = 1000L
const val MILLIS_IN_MINUTE = SECONDS_IN_MINUTE * MILLIS_IN_SECOND

val ddMMyyyyHHmmFormatDate = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
val formatFromServer = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
val mmSSFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

fun Long.toMmSsFormat(): String {
    return mmSSFormat.format(this)
}