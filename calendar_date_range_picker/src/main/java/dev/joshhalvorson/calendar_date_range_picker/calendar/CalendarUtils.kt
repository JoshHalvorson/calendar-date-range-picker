package dev.joshhalvorson.calendar_date_range_picker.calendar

import java.time.Year

// functions to determine the day of the week any given date is
object CalendarUtils {
    fun getDayOfWeekOfDate(year: Int, month: Int, date: Int): Int {
        return (getYearCode(year) + getMonthCode(month) + getCenturyCode(year) + date - (if (isLeapYear(year, month)) 1 else 0)) % 7
    }

    fun getYearCode(year: Int): Int {
        val lastTwoDigits = year.toString().takeLast(2).toInt()
        return (lastTwoDigits + (lastTwoDigits.div(4))) % 7
    }

    fun getMonthCode(month: Int): Int {
        return when (month) {
            0 -> 0
            1 -> 3
            2 -> 3
            3 -> 6
            4 -> 1
            5 -> 4
            6 -> 6
            7 -> 2
            8 -> 5
            9 -> 0
            10 -> 3
            11 -> 5
            else -> -1
        }
    }

    fun getCenturyCode(year: Int): Int {
        return when (year) {
            in 2000..2099 -> 6
            else -> -1
        }
    }

    fun isLeapYear(year: Int, month: Int): Boolean {
        val leapMonth = when (month) {
            0 -> true
            1 -> true
            else -> false
        }

        if (!leapMonth) return false

        val leapYear = ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)

        if (!leapYear) return false

        return true
    }

    object Constants {
        val STRING_MONTHS_TO_INT_MONTHS = mapOf<String, Int>(
            "January" to 0,
            "February" to 1,
            "March" to 2,
            "April" to 3,
            "May" to 4,
            "June" to 5,
            "July" to 6,
            "August" to 7,
            "September" to 8,
            "October" to 9,
            "November" to 10,
            "December" to 11
        )

        val INT_MONTHS_TO_STRING_MONTHS = mapOf<Int, String>(
            0 to "January",
            1 to "February",
            2 to "March",
            3 to "April",
            4 to "May",
            5 to "June",
            6 to "July",
            7 to "August",
            8 to "September",
            9 to "October",
            10 to "November",
            11 to "December",
        )

        val daysInMonth = arrayOf(
            "Jan" to 31,
            "Feb" to if (isLeapYear(Year.now().value, 1)) 29 else 28,
            "Mar" to 31,
            "Apr" to 30,
            "May" to 31,
            "Jun" to 30,
            "Jul" to 31,
            "Aug" to 31,
            "Sep" to 30,
            "Oct" to 31,
            "Nov" to 30,
            "Dec" to 31
        )

        val daysInIntMonth = arrayOf(
            0 to 31,
            1 to if (isLeapYear(Year.now().value, 1)) 29 else 28,
            2 to 31,
            3 to 30,
            4 to 31,
            5 to 30,
            6 to 31,
            7 to 31,
            8 to 30,
            9 to 31,
            10 to 30,
            11 to 31
        )
    }
}