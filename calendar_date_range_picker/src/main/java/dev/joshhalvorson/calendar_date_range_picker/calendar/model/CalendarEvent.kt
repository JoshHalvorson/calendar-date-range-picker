package dev.joshhalvorson.calendar_date_range_picker.calendar.model

import java.util.*

/**
 * Object for add events to the calendar
 *
 * @param eventName name of the event as a String
 * @param eventDescription description of the event as a String
 * @param date is a Date object for add the event dot on the given date, this is created by
 * instantiating a Calendar object and accessing its date object with calendarInstance.time
 * (eg: Calendar.getInstance().time)
 *
 */
data class CalendarEvent(
    val eventName: String,
    val eventDescription: String,
    val date: Date
)