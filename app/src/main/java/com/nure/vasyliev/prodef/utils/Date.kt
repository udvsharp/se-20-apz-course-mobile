package com.nure.vasyliev.prodef.utils

import java.text.SimpleDateFormat
import java.util.Locale

val ddMMyyyyHHmmFormatDate = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
val formatFromServer = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())