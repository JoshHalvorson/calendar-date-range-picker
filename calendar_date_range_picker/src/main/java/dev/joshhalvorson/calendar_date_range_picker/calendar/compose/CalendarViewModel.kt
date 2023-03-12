package dev.joshhalvorson.calendar_date_range_picker.calendar.compose

import android.util.Log
import androidx.lifecycle.ViewModel
import dev.joshhalvorson.calendar_date_range_picker.calendar.compose.model.DateRange
import dev.joshhalvorson.calendar_date_range_picker.calendar.compose.model.FormattedDate
import dev.joshhalvorson.calendar_date_range_picker.calendar.compose.model.PickedDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarViewModel : ViewModel() {
    private val _firstSelectedDate = MutableStateFlow<LocalDate>(LocalDate.MIN)
    val firstSelectedDate = _firstSelectedDate.asStateFlow()

    private val _secondSelectedDate = MutableStateFlow<LocalDate>(LocalDate.MIN)
    val secondSelectedDate = _secondSelectedDate.asStateFlow()

    private val _pickedDate = MutableStateFlow(PickedDate(date = LocalDate.MIN, formattedDate = FormattedDate("")))
    val pickedDate = _pickedDate.asStateFlow()

    private val _dateRange = MutableStateFlow(DateRange(null))
    val dateRange = _dateRange.asStateFlow()

    fun onDateClicked(date: LocalDate, calendarType: CalendarType): Pair<LocalDate, LocalDate> {
        when (calendarType) {
            CalendarType.Single -> {
                _firstSelectedDate.value = date
            }
            CalendarType.Range -> {
                // If no selected dates, set first selected date
                // If two selected dates, set first selected date and reset second selected date
                // Otherwise, set second selected date
                val firstDateNotSet = _firstSelectedDate.value == LocalDate.MIN
                val secondDateSet = _secondSelectedDate.value != LocalDate.MIN
                val newDateIsBeforeFirstDate = date.isBefore(_firstSelectedDate.value)

                if (firstDateNotSet || secondDateSet || newDateIsBeforeFirstDate) {
                    _firstSelectedDate.value = date
                    _secondSelectedDate.value = LocalDate.MIN
                } else {
                    if (_firstSelectedDate.value.toString() == date.toString()) {
                        _firstSelectedDate.value = date
                        _secondSelectedDate.value = LocalDate.MIN
                    } else {
                        _secondSelectedDate.value = date
                    }
                }
            }
        }

        Log.i(
            "onDateClicked",
            "firstDate: ${firstSelectedDate.value} \nsecondDate: ${secondSelectedDate.value}"
        )
        return Pair(_firstSelectedDate.value, _secondSelectedDate.value)
    }

    fun getPickedDate(date: LocalDate, outputFormat: DateTimeFormatter): PickedDate {
        val newPickedDate = PickedDate(
            date = date,
            formattedDate = FormattedDate(value = date.format(outputFormat))
        )

        _pickedDate.value = newPickedDate

        return newPickedDate
    }

    fun resetRange() {
        _dateRange.value = DateRange(null)
    }

    fun getDateRange(
        dates: Pair<LocalDate, LocalDate>,
        outputFormat: DateTimeFormatter
    ): DateRange {
        val firstDate = dates.first
        val secondDate = dates.second

        val firstDateFormatted = FormattedDate(
            value = firstDate.format(outputFormat)
        )
        val secondDateFormatted = FormattedDate(
            value = secondDate.format(outputFormat)
        )

        val newDateRange = DateRange(
            dates = Pair(
                PickedDate(
                    date = firstDate,
                    formattedDate = firstDateFormatted
                ),
                PickedDate(
                    date = secondDate,
                    formattedDate = secondDateFormatted
                )
            )
        )

        _dateRange.value = newDateRange

        return newDateRange
    }
}