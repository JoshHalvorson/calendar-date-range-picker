package dev.joshhalvorson.calendar_date_range_picker.calendar

/** Utility class */
internal object CalendarUtils {
    private const val JANUARY = "January"
    private const val FEBRUARY =  "February"
    private const val MARCH =  "March"
    private const val APRIL =  "April"
    private const val MAY =  "May"
    private const val JUNE =  "June"
    private const val JULY =  "July"
    private const val AUGUST =  "August"
    private const val SEPTEMBER =  "September"
    private const val OCTOBER =  "October"
    private const val NOVEMBER =  "November"
    private const val DECEMBER =  "December"

    /**
     * Determines the day of the week for the given date.
     *
     * @return an index representing the day of the week where 1 is monday, 2 tuesday, etc.
     */
    internal fun getDayOfWeekOfDate(year: Int, month: Int, date: Int): Int {
        return (getYearCode(year) + getMonthCode(month) + getCenturyCode(year) + date - (if (isLeapYear(
                year
            )
        ) 1 else 0)) % 7
    }

    private fun getYearCode(year: Int): Int {
        val lastTwoDigits = year.toString().takeLast(2).toInt()
        return (lastTwoDigits + (lastTwoDigits.div(4))) % 7
    }

    private fun getMonthCode(month: Int): Int {
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

    private fun getCenturyCode(year: Int): Int {
        return when (year) {
            in 2000..2099 -> 6
            else -> -1
        }
    }

    internal fun isLeapYear(year: Int): Boolean {
        return when {
            year % 4 != 0 -> {
                false
            }
            year % 100 != 0 -> {
                true
            }
            year % 400 != 0 -> {
                false
            }
            else -> {
                true
            }
        }
    }

    internal fun getDaysInMonth(month: Int, year: Int): Int? {
        val daysInMonth = mapOf(
            0 to 31,
            1 to if (isLeapYear(year)) 29 else 28,
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

        return daysInMonth[month]
    }

    object Constants {
        val STRING_MONTHS_TO_INT_MONTHS = mapOf<String, Int>(
            JANUARY to 0,
            FEBRUARY to 1,
            MARCH to 2,
            APRIL to 3,
            MAY to 4,
            JUNE to 5,
            JULY to 6,
            AUGUST to 7,
            SEPTEMBER to 8,
            OCTOBER to 9,
            NOVEMBER to 10,
            DECEMBER to 11
        )

        val INT_MONTHS_TO_STRING_MONTHS = mapOf<Int, String>(
            0 to JANUARY,
            1 to FEBRUARY,
            2 to MARCH,
            3 to APRIL,
            4 to MAY,
            5 to JUNE,
            6 to JULY,
            7 to AUGUST,
            8 to SEPTEMBER,
            9 to OCTOBER,
            10 to NOVEMBER,
            11 to DECEMBER,
        )
    }
}