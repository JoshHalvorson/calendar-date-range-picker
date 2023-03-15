package dev.joshhalvorson.calendar_date_range_picker.calendar.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.joshhalvorson.calendar_date_range_picker.calendar.CalendarUtils
import dev.joshhalvorson.calendar_date_range_picker.calendar.compose.model.DateRange
import dev.joshhalvorson.calendar_date_range_picker.calendar.compose.model.PickedDate
import java.time.LocalDate

@Composable
fun CalendarPicker(
    calendarOptions: CalendarOptions = CalendarDefaults.calendarOptions(),
    daysOfWeekOptions: DaysOfWeekOptions = CalendarDefaults.daysOfWeek(),
    dayNumberOptions: DayNumberOptions = CalendarDefaults.dayNumber(),
    headerContent: @Composable (pickedDate: PickedDate, currentMonthAndYear: String) -> Unit = { pickedDate, currentMonthAndYear ->
        Column {
            Text(text = "Select Date", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = pickedDate.formattedDate.value)
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(12.dp))
        }
    },
    onDateClicked: (pickedDate: PickedDate) -> Unit = { },
) {
    val viewModel: CalendarViewModel = viewModel(key = "singleViewModel")
    BaseCalendarPicker(
        viewModel = viewModel,
        calendarType = CalendarType.Single,
        calendarOptions = calendarOptions,
        daysOfWeekOptions = daysOfWeekOptions,
        dayNumberOptions = dayNumberOptions,
        onDateClicked = onDateClicked,
        onDateRangeClicked = { },
        headerContent = headerContent,
        dateRangeHeaderContent = { }
    )
}

@Composable
fun CalendarRangePicker(
    calendarOptions: CalendarOptions = CalendarDefaults.calendarOptions(),
    daysOfWeekOptions: DaysOfWeekOptions = CalendarDefaults.daysOfWeek(),
    dayNumberOptions: DayNumberOptions = CalendarRangeDefaults.dayNumber(),
    headerContent: @Composable (DateRange) -> Unit = {
        Column {
            Text(text = "Select Dates", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            if (it.hasDates()) {
                Text(text = "${it.firstFormattedDate} to ${it.secondFormattedDate}")
            } else {
                Text(text = " ")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(12.dp))
        }
    },
    onDateRangeClicked: (DateRange) -> Unit = { }
) {
    val viewModel: CalendarViewModel = viewModel(key = "rangeViewModel")
    BaseCalendarPicker(
        viewModel = viewModel,
        calendarType = CalendarType.Range,
        calendarOptions = calendarOptions,
        daysOfWeekOptions = daysOfWeekOptions,
        dayNumberOptions = dayNumberOptions,
        onDateClicked = { },
        onDateRangeClicked = onDateRangeClicked,
        headerContent = { _, _ -> },
        dateRangeHeaderContent = headerContent
    )
}

@Composable
private fun BaseCalendarPicker(
    viewModel: CalendarViewModel,
    headerContent: @Composable (pickedDate: PickedDate, currentMonthAndYear: String) -> Unit,
    dateRangeHeaderContent: @Composable (DateRange) -> Unit,
    calendarType: CalendarType,
    calendarOptions: CalendarOptions,
    daysOfWeekOptions: DaysOfWeekOptions,
    dayNumberOptions: DayNumberOptions,
    onDateClicked: (PickedDate) -> Unit,
    onDateRangeClicked: (DateRange) -> Unit,
) {
    val firstSelectedDate by viewModel.firstSelectedDate.collectAsState()
    val secondSelectedDate by viewModel.secondSelectedDate.collectAsState()
    val dateRange by viewModel.dateRange.collectAsState()
    val pickedDate by viewModel.pickedDate.collectAsState()

    val month = calendarOptions.initialDate().value.monthValue - 1
    val year = calendarOptions.initialDate().value.year
    val daysInCurrentMonth = CalendarUtils.getDaysInMonth(month, year) ?: return
    val daysList: MutableList<Int> = mutableListOf<Int>().apply {
        for (d in 1..daysInCurrentMonth) {
            this.add(d)
        }
    }
    val firstDay = CalendarUtils.getDayOfWeekOfDate(year, month, 1)
    for (i in 1..firstDay) {
        daysList.add(-1)
    }
    val daysOfWeek = listOf(
        "Sunday",
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
    )

    MaterialTheme {
        Column {
            if (calendarType == CalendarType.Single) {
                headerContent(pickedDate, "${CalendarUtils.Constants.INT_MONTHS_TO_STRING_MONTHS[month]} $year")
            } else {
                dateRangeHeaderContent(dateRange)
            }

            Calendar(
                daysOfWeek = daysOfWeek,
                daysOfWeekOptions = daysOfWeekOptions,
                daysList = daysList,
                year = year,
                month = month,
                calendarType = calendarType,
                firstSelectedDate = firstSelectedDate,
                secondSelectedDate = secondSelectedDate,
                calendarOptions = calendarOptions,
                dayNumberOptions = dayNumberOptions,
                viewModel = viewModel,
                onDateClicked = onDateClicked,
                onDateRangeClicked = onDateRangeClicked,
            )
        }
    }
}

@Composable
private fun Calendar(
    daysOfWeek: List<String>,
    daysOfWeekOptions: DaysOfWeekOptions,
    daysList: MutableList<Int>,
    year: Int,
    month: Int,
    calendarType: CalendarType,
    firstSelectedDate: LocalDate,
    secondSelectedDate: LocalDate,
    calendarOptions: CalendarOptions,
    dayNumberOptions: DayNumberOptions,
    viewModel: CalendarViewModel,
    onDateClicked: (PickedDate) -> Unit,
    onDateRangeClicked: (DateRange) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        columns = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(daysOfWeek) {
            DayOfWeek(text = it, daysOfWeekOptions = daysOfWeekOptions)
        }

        itemsIndexed(daysList.sorted()) { index, item ->
            if (item != -1) {
                val itemDate = LocalDate.of(year, month + 1, item)

                val isSelectedDate = when (calendarType) {
                    CalendarType.Single -> {
                        itemDate.equals(firstSelectedDate)
                    }
                    CalendarType.Range -> {
                        (itemDate.isEqual(firstSelectedDate) || itemDate.isAfter(
                            firstSelectedDate
                        ) && (itemDate.isEqual(secondSelectedDate) || itemDate.isBefore(
                            secondSelectedDate
                        )))
                    }
                }

                Date(
                    firstSelectedDate = firstSelectedDate,
                    secondSelectedDate = secondSelectedDate,
                    calendarType = calendarType,
                    date = item,
                    isSelectedDate = isSelectedDate,
                    calendarOptions = calendarOptions,
                    daysOfWeekOptions = daysOfWeekOptions,
                    dayNumberOptions = dayNumberOptions,
                    onDateClicked = { date ->
                        val selectedDates = viewModel.onDateClicked(date, calendarType)
                        val firstDate = selectedDates.first
                        val secondDate = selectedDates.second

                        when (calendarType) {
                            CalendarType.Single -> {
                                onDateClicked(
                                    viewModel.getPickedDate(
                                        date = date,
                                        outputFormat = calendarOptions.outputFormat()
                                    )
                                )
                            }
                            CalendarType.Range -> {
                                if (firstDate != LocalDate.MIN && secondDate != LocalDate.MIN) {
                                    onDateRangeClicked(
                                        viewModel.getDateRange(
                                            dates = Pair(firstDate, secondDate),
                                            outputFormat = calendarOptions.outputFormat()
                                        )
                                    )
                                } else {
                                    viewModel.resetRange()
                                    onDateRangeClicked(DateRange(dates = null))
                                }
                            }
                        }
                    }
                )
            } else {
                Spacer(modifier = Modifier)
            }
        }
    }
}

@Composable
private fun Date(
    modifier: Modifier = Modifier,
    firstSelectedDate: LocalDate,
    secondSelectedDate: LocalDate,
    calendarType: CalendarType,
    date: Int,
    isSelectedDate: Boolean,
    calendarOptions: CalendarOptions,
    daysOfWeekOptions: DaysOfWeekOptions,
    dayNumberOptions: DayNumberOptions,
    onDateClicked: (LocalDate) -> Unit
) {
    val month = calendarOptions.initialDate().value.monthValue - 1
    val year = calendarOptions.initialDate().value.year
    val itemDate = LocalDate.of(year, month + 1, date)
    val minDate =
        if (calendarOptions.minDate() != Long.MIN_VALUE) LocalDate.ofEpochDay(calendarOptions.minDate()) else LocalDate.MIN
    val maxDate =
        if (calendarOptions.maxDate() != Long.MAX_VALUE) LocalDate.ofEpochDay(calendarOptions.maxDate()) else LocalDate.MAX
    val isEnabledDate =
        (itemDate.isAfter(minDate) || itemDate.isEqual(minDate)) && (itemDate.isBefore(maxDate) || itemDate.isEqual(
            maxDate
        ))
    val isAvailableDate = calendarOptions.availableDates().contains(itemDate.toEpochDay())
    val backgroundStyle = if (isSelectedDate) {
        dayNumberOptions.selectedBackgroundStyle()
    } else if (isAvailableDate) {
        dayNumberOptions.availableBackgroundStyle()
    } else {
        dayNumberOptions.backgroundStyle()
    }
    val color = backgroundStyle.color ?: MaterialTheme.colorScheme.secondaryContainer
    var shape = backgroundStyle.shape ?: CircleShape

    if (calendarType == CalendarType.Range) {
        val isFirstDate = firstSelectedDate.isEqual(itemDate)
        val isSecondDate = secondSelectedDate.isEqual(itemDate)
        val isItemInRange =
            (itemDate.isAfter(firstSelectedDate) || itemDate.isEqual(firstSelectedDate)) && (itemDate.isBefore(
                secondSelectedDate
            ) || itemDate.isEqual(secondSelectedDate))

        if (isItemInRange) {
            shape = if (isFirstDate && secondSelectedDate.isEqual(LocalDate.MIN)) {
                CircleShape
            } else if (isFirstDate) {
                RoundedCornerShape(
                    topStartPercent = 100,
                    topEndPercent = 0,
                    bottomStartPercent = 100,
                    bottomEndPercent = 0
                )
            } else if (isSecondDate) {
                RoundedCornerShape(
                    topStartPercent = 0,
                    topEndPercent = 100,
                    bottomStartPercent = 0,
                    bottomEndPercent = 100
                )
            } else {
                RectangleShape
            }
        }
    }

    val backgroundModifier = Modifier
        .background(color = color, shape = shape)
        .clip(shape)

    val disabledStyle =
        LocalTextStyle.current.copy(color = LocalContentColor.current.copy(alpha = .5f))

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (isSelectedDate || isAvailableDate) {
            Box(modifier = backgroundModifier.size(30.dp))
        }

        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(shape)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    enabled = isEnabledDate,
                    indication = null,
                    onClick = {
                        onDateClicked(itemDate)
                    }
                )
        )

        if (daysOfWeekOptions.customContent() == null) {
            Text(
                text = "$date",
                fontSize = 12.sp,
                style = if (isEnabledDate) {
                    if (isSelectedDate) {
                        dayNumberOptions.selectedStyle()
                    } else if (isAvailableDate) {
                        dayNumberOptions.availableStyle()
                    } else {
                        dayNumberOptions.style()
                    }
                } else {
                    disabledStyle
                },
                textAlign = TextAlign.Center,
            )
        } else {
            dayNumberOptions.customContent()?.invoke("$date")
        }
    }
}

@Composable
private fun DayOfWeek(
    text: String,
    daysOfWeekOptions: DaysOfWeekOptions
) {
    val dayText = when (daysOfWeekOptions.type()) {
        DaysOfWeekType.First -> text.first().toString()
        DaysOfWeekType.Three -> text.substring(0..2).toString()
        DaysOfWeekType.Whole -> text
    }
    if (daysOfWeekOptions.customContent() == null) {
        Text(
            text = if (daysOfWeekOptions.allCaps()) dayText.uppercase() else dayText,
            fontSize = 12.sp,
            style = daysOfWeekOptions.style(),
            textAlign = TextAlign.Center
        )
    } else {
        daysOfWeekOptions.customContent()?.invoke(dayText)
    }
}