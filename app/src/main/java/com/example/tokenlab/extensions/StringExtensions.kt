
package com.example.tokenlab.extensions

import android.util.Patterns
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


fun String.isValidEmail(): Boolean {
    return this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.convertToValidDateFormat(): String {
    val localeBr = Locale("pt", "BR")
    val dateFormatDayMonthYear = "dd/MM/yyyy"
    val dateFormatYearMonthDay = "yyyy-MM-dd"


    val format = SimpleDateFormat(dateFormatYearMonthDay, localeBr)
    val date: Date = format.parse(this) as Date

    val dateFormat: DateFormat = SimpleDateFormat(dateFormatDayMonthYear, localeBr)
    return dateFormat.format(date)
}
