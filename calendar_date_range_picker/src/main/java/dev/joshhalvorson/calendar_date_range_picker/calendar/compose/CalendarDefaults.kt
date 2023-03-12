package dev.joshhalvorson.calendar_date_range_picker.calendar.compose

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import dev.joshhalvorson.calendar_date_range_picker.calendar.compose.model.BackgroundStyle
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object CalendarDefaults {
    @Composable
    fun calendarOptions(
        initialDate: LocalDate = LocalDate.now(),
        outputFormat: DateTimeFormatter = DateTimeFormatter.ISO_DATE,
        minDate: Long = Long.MIN_VALUE,
        maxDate: Long = Long.MAX_VALUE,
        highlightedDates: List<Long> = emptyList(),
    ) = CalendarOptions(
        initialDate = initialDate,
        outputFormat = outputFormat,
        minDate = minDate,
        maxDate = maxDate,
        highlightedDates = highlightedDates,
    )

    @Composable
    fun daysOfWeek(
        type: DaysOfWeekType = DaysOfWeekType.First,
        style: TextStyle = LocalTextStyle.current,
        customContent: (@Composable (String) -> Unit)? = null
    ) = DaysOfWeekOptions(
        type = type,
        style = style,
        customContent = customContent,
    )

    @Composable
    fun dayNumber(
        style: TextStyle = LocalTextStyle.current,
        backgroundStyle: BackgroundStyle = BackgroundStyle(
            color = Color.Unspecified,
            shape = CircleShape,
        ),
        selectedStyle: TextStyle = LocalTextStyle.current,
        selectedBackgroundStyle: BackgroundStyle = BackgroundStyle(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = CircleShape,
        ),
        customContent: (@Composable (String) -> Unit)? = null,
    ) = DayNumberOptions(
        style = style,
        backgroundStyle = backgroundStyle,
        selectedStyle = selectedStyle,
        selectedBackgroundStyle = selectedBackgroundStyle,
        customContent = customContent,
    )
}

object CalendarRangeDefaults {
    @Composable
    fun dayNumber(
        style: TextStyle = LocalTextStyle.current,
        selectedStyle: TextStyle = LocalTextStyle.current,
        selectedBackgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
        customContent: (@Composable (String) -> Unit)? = null,
    ) = DayNumberOptions(
        style = style,
        backgroundStyle = BackgroundStyle(color = null, shape = null),
        selectedStyle = selectedStyle,
        selectedBackgroundStyle = BackgroundStyle(
            color = selectedBackgroundColor,
            shape = null
        ),
        customContent = customContent,
    )
}

class CalendarOptions(
    private val initialDate: LocalDate,
    private val outputFormat: DateTimeFormatter,
    private val minDate: Long,
    private val maxDate: Long,
    private val highlightedDates: List<Long>
) {
    @Composable
    internal fun initialDate() = initialDate

    internal fun outputFormat() = outputFormat

    internal fun highlightedDates() = highlightedDates

    @Composable
    internal fun minDate() = minDate

    @Composable
    internal fun maxDate() = maxDate
}

class DayNumberOptions(
    private val style: TextStyle,
    private val backgroundStyle: BackgroundStyle,
    private val selectedStyle: TextStyle,
    private val selectedBackgroundStyle: BackgroundStyle,
    private val customContent: (@Composable (String) -> Unit)?,
) {
    @Composable
    internal fun style() = style

    @Composable
    internal fun selectedStyle() = selectedStyle

    @Composable
    internal fun customContent() = customContent

    @Composable
    internal fun backgroundStyle() = backgroundStyle

    @Composable
    internal fun selectedBackgroundStyle() = selectedBackgroundStyle
}

class DaysOfWeekOptions(
    private val type: DaysOfWeekType,
    private val style: TextStyle,
    private val customContent: (@Composable (String) -> Unit)?,
) {
    @Composable
    internal fun type() = type

    @Composable
    internal fun style() = style

    @Composable
    internal fun customContent() = customContent
}

enum class DaysOfWeekType {
    First,
    Three,
    Whole;
}

enum class CalendarType {
    Single,
    Range
}