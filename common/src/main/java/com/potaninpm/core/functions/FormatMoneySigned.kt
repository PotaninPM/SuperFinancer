package com.potaninpm.core.functions

import java.text.NumberFormat
import java.util.Locale

fun formatMoneySigned(value: Double): String {
    val numberFormat = NumberFormat.getInstance(Locale("ru", "RU"))
    val formattedValue = numberFormat.format(value)

    return if (value >= 0) {
        "+$formattedValue ₽"
    } else {
        "$formattedValue ₽"
    }
}

