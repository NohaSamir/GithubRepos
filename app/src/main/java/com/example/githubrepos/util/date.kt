package com.example.githubrepos.util

import java.text.SimpleDateFormat
import java.util.*

fun formatServerDate(date: String): String {

    val inputFormatter =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)

    val outputFormatter = SimpleDateFormat("dd-MM-yyy", Locale.ENGLISH)

    return outputFormatter.format(inputFormatter.parse(date))
}