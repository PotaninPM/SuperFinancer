package com.potaninpm.core.functions

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight

fun markdownToString(str: String): AnnotatedString {
    val builder = AnnotatedString.Builder()
    val lines = str.lines()
    val boldRegex = Regex("\\*\\*(.*?)\\*\\*")

    lines.forEachIndexed { _, line ->
        // Обработка буллетов: если строка начинается с дефиса, заменяем его на "•"
        val goLine = if (line.trimStart().startsWith("-")) {
            "• ${line.trimStart().removePrefix("-")}"
        } else {
            line
        }

        var curIndex = 0
        boldRegex.findAll(goLine).forEach { res ->
            val start = res.range.first

            // Добавляем текст до жирного сегмента
            if (start > curIndex) {
                builder.append(goLine.substring(curIndex, start))
            }
            val boldText = res.groupValues[1]

            // Применяем стиль жирного текста
            builder.pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
            builder.append(boldText)
            builder.pop()
            curIndex = res.range.last + 1
        }

        // Добавляем оставшийся текст в строке после жирных сегментов
        if (curIndex < goLine.length) {
            builder.append(goLine.substring(curIndex))
        }

        // Добавляем символ новой строки после всей строки
        builder.append("\n")
    }

    return builder.toAnnotatedString()
}


