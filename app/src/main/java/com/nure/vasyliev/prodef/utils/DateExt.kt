package com.nure.vasyliev.prodef.utils

import java.text.SimpleDateFormat
import java.util.Locale

const val MILLIS_IN_SECOND = 1000L

val ddMMyyyyHHmmFormatDate = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
val formatFromServer = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
val mmSSFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

fun Long.toMmSsFormat(): String {
    return mmSSFormat.format(this)
}