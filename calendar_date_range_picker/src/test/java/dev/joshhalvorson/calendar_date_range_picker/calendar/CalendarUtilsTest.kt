package dev.joshhalvorson.calendar_date_range_picker.calendar

import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test


internal class CalendarUtilsTest {

    private val mondayCode = 1
    private val tuesdayCode = 2
    private val wednesdayCode = 3
    private val thursdayCode = 4
    private val fridayCode = 5
    private val saturdayCode = 6
    private val sundayCode = 7

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getDayOfWeekOfDateShouldSuccess() {
        val year = 2021
        val august = 7 // 0-based index
        val september = 8 // 0-based index

        // yyyy-mm-dd: 2021-08-30 is monday
        assertThat(CalendarUtils.getDayOfWeekOfDate(year, august, 30)).isEqualTo(mondayCode)

        // yyyy-mm-dd: 2021-08-31 is tuesday
        assertThat(CalendarUtils.getDayOfWeekOfDate(year, august, 31)).isEqualTo(tuesdayCode)

        // yyyy-mm-dd: 2021-09-01 is wednesday
        assertThat(CalendarUtils.getDayOfWeekOfDate(year, september, 1)).isEqualTo(wednesdayCode)

        // yyyy-mm-dd: 2021-09-02 is thursday
        assertThat(CalendarUtils.getDayOfWeekOfDate(year, september, 2)).isEqualTo(thursdayCode)

        // yyyy-mm-dd: 2021-09-02 is friday
        assertThat(CalendarUtils.getDayOfWeekOfDate(year, september, 3)).isEqualTo(fridayCode)
    }

    @Test
    fun getDayOfWeekOfDateShouldSuccessButIsFailing() {
        val year = 2021
        val august = 7 // 0-based index

        // this test if failing because CalendarUtils.isLeapYear(year, month) is buggy
        // see comments in test: isLeapYearShouldSuccessButIsFailing

        // yyyy-mm-dd: 2020-02-31 is friday
        assertThat(CalendarUtils.getDayOfWeekOfDate(year, august, 31)).isEqualTo(fridayCode)
    }

    @Test
    fun isLeapYearShouldSuccessButIsFailing() {
        // this test was added as a proof that CalendarUtils.isLeapYear(year, month) is buggy
        // as an alternative I added CalendarUtils.isLeapYear(year).
        // If PR is OK we will migrate the code to use the new isLeapYear and remove this test.
        val leapYear = 2020
        val month = 2

        assertThat(CalendarUtils.isLeapYear(leapYear, month)).isTrue()
    }

    @Test
    fun isLeapYearSuccessOnAllMonths() {
        val leapYear = 2020

        assertThat(CalendarUtils.isLeapYear(leapYear)).isTrue()
    }

    @Test
    fun isLeapYearFailureOnAllMonths() {
        val leapYear = 2021

        assertThat(CalendarUtils.isLeapYear(leapYear)).isFalse()
    }
}