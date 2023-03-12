package dev.joshhalvorson.calendar_date_range_picker.calendar.compose.model

import java.time.LocalDate

data class PickedDate(
    val date: LocalDate,
    val formattedDate: FormattedDate
)