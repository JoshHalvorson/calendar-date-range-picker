package dev.joshhalvorson.calendar_date_range_picker.calendar.compose.model

data class DateRange(
    val dates: Pair<PickedDate, PickedDate>?
) {
    fun hasDates() = dates != null

    val firstDate: PickedDate? = dates?.first
    val secondDate: PickedDate? = dates?.second

    val firstFormattedDate: String = if (hasDates()) dates!!.first.formattedDate.value else ""
    val secondFormattedDate: String = if (hasDates()) dates!!.second.formattedDate.value else ""
}