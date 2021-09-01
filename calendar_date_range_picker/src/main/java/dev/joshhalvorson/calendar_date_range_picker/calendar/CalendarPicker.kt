package dev.joshhalvorson.calendar_date_range_picker.calendar

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.forEach
import dev.joshhalvorson.calendar_date_range_picker.R
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

class CalendarPicker(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    companion object {
        const val TAG = "CalendarPicker"
    }

    private val sdf = SimpleDateFormat("M/dd/yyyy", Locale.US)

    private var textViews = mutableListOf<TextView>()
    private var firstSelectedDate = mutableMapOf<String?, Int?>()
    private var secondSelectedDate = mutableMapOf<String?, Int?>()
    private var selectedDateRange = Pair(0L, 0L)
    private var currentMonth = Calendar.getInstance().get(2)
    private var currentYear = Year.now().value
    private var selectedYear = currentYear

    private val titleText: TextView by lazy { findViewById(R.id.titleText) }
    private val yearButton: Button by lazy { findViewById(R.id.yearButton) }
    private val dateRangeText: TextView by lazy { findViewById(R.id.dateRangeText) }
    private val prevMonthButton: ImageButton by lazy { findViewById(R.id.prevMonthButton) }
    private val nextMonthButton: ImageButton by lazy { findViewById(R.id.nextMonthButton) }
    private val daysOfWeekLinearLayout: LinearLayout by lazy { findViewById(R.id.daysOfWeekLinearLayout) }
    private val yearLinearLayout: LinearLayout by lazy { findViewById(R.id.yearLinearLayout) }
    private val daysLinearLayout: LinearLayout by lazy { findViewById(R.id.daysLinearLayout) }

    var dayTextColor: Int? = null
    var yearTextColor: Int? = null
    var selectedDateCircleColor: Int? = null
    var highlightedDatesBackgroundColor: Int? = null
    var highlightedDatesTextColor: Int? = null
    var titleColor: Int? = null
    var nextMonthButtonColor: Int? = null
    var previousMonthButtonColor: Int? = null
    var yearSelectButtonColor: Int? = null
    var yearSelectButtonTextColor: Int? = null
    var firstSelectedDateTextColor: Int? = null
    var secondSelectedDateTextColor: Int? = null
    var dateRangeTextColor: Int? = null
    var daysOfWeekTextColor: Int? = null
    var eventDotColor: Int? = null
    var eventDotColorWhenHighlighted: Int? = null
    var eventDotColorWhenSelected: Int? = null
    var previousMonthButtonIcon: Drawable? = null
    var nextMonthButtonIcon: Drawable? = null

    val calendarEvents = mutableListOf<Pair<String, Calendar>>()
    val tvsWithEvent = mutableListOf<String>()

    init {
        inflate(context, R.layout.calendar, this)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CalendarPicker)
        getColorAttributes(attributes)
        getDrawableAttributes(attributes)
        attributes.recycle()

        setColors()
        setDrawables()
        initCalendar()
        initYearsCalendar()
    }

    fun getSelectedDates(): Pair<Long, Long>? {
        val validated = isValidated(
            dateRangeText.text.split("-")[0].replace(
                "\\s".toRegex(),
                ""
            ),
            dateRangeText.text.split("-")[1].replace(
                "\\s".toRegex(),
                ""
            )
        )
        if (validated.first) {
            selectedDateRange = validated.second
            return selectedDateRange

        }

        return null
    }

    fun addEvents(vararg events: Pair<String, Calendar>) {
        calendarEvents.addAll(events)
        initCalendar()
    }

    private fun getColorAttributes(attributes: TypedArray) {
        dayTextColor = attributes.getColor(
            R.styleable.CalendarPicker_dayTextColor,
            ResourcesCompat.getColor(resources, R.color.blue_three, null)
        )
        yearTextColor = attributes.getColor(
            R.styleable.CalendarPicker_yearTextColor,
            ResourcesCompat.getColor(resources, R.color.blue_three, null)
        )
        selectedDateCircleColor = attributes.getColor(
            R.styleable.CalendarPicker_selectedDateCircleColor,
            ResourcesCompat.getColor(resources, R.color.blue_one, null)
        )
        highlightedDatesBackgroundColor = attributes.getColor(
            R.styleable.CalendarPicker_highlightedDatesBackgroundColor,
            ResourcesCompat.getColor(resources, R.color.blue_four, null)
        )
        highlightedDatesTextColor = attributes.getColor(
            R.styleable.CalendarPicker_highlightedDatesTextColor,
            ResourcesCompat.getColor(resources, R.color.blue_one, null)
        )
        titleColor = attributes.getColor(
            R.styleable.CalendarPicker_titleColor,
            ResourcesCompat.getColor(resources, R.color.blue_one, null)
        )
        nextMonthButtonColor = attributes.getColor(
            R.styleable.CalendarPicker_nextMonthButtonColor,
            ResourcesCompat.getColor(resources, R.color.white, null)
        )
        previousMonthButtonColor = attributes.getColor(
            R.styleable.CalendarPicker_previousMonthButtonColor,
            ResourcesCompat.getColor(resources, R.color.white, null)
        )
        yearSelectButtonColor = attributes.getColor(
            R.styleable.CalendarPicker_yearSelectButtonColor,
            ResourcesCompat.getColor(resources, R.color.white, null)
        )
        yearSelectButtonTextColor = attributes.getColor(
            R.styleable.CalendarPicker_yearSelectButtonTextColor,
            ResourcesCompat.getColor(resources, R.color.blue_one, null)
        )
        firstSelectedDateTextColor = attributes.getColor(
            R.styleable.CalendarPicker_firstSelectedDateTextColor,
            ResourcesCompat.getColor(resources, R.color.white, null)
        )
        secondSelectedDateTextColor = attributes.getColor(
            R.styleable.CalendarPicker_secondSelectedDateTextColor,
            ResourcesCompat.getColor(resources, R.color.white, null)
        )
        dateRangeTextColor = attributes.getColor(
            R.styleable.CalendarPicker_dateRangeTextColor,
            ResourcesCompat.getColor(resources, R.color.grey_two, null)
        )
        daysOfWeekTextColor = attributes.getColor(
            R.styleable.CalendarPicker_daysOfWeekTextColor,
            ResourcesCompat.getColor(resources, R.color.grey_two, null)
        )
        eventDotColor = attributes.getColor(
            R.styleable.CalendarPicker_eventDotColor,
            ResourcesCompat.getColor(resources, R.color.grey_two, null)
        )
        eventDotColorWhenHighlighted = attributes.getColor(
            R.styleable.CalendarPicker_eventDotColorWhenHighlighted,
            ResourcesCompat.getColor(resources, R.color.grey_two, null)
        )
        eventDotColorWhenSelected = attributes.getColor(
            R.styleable.CalendarPicker_eventDotColorWhenSelected,
            ResourcesCompat.getColor(resources, R.color.grey_two, null)
        )
    }

    private fun getDrawableAttributes(attributes: TypedArray) {
        previousMonthButtonIcon =
            attributes.getDrawable(R.styleable.CalendarPicker_previousMonthButtonIcon)
                ?: ResourcesCompat.getDrawable(resources, R.drawable.ic_chevron_left, null)
        nextMonthButtonIcon = attributes.getDrawable(R.styleable.CalendarPicker_nextMonthButtonIcon)
            ?: ResourcesCompat.getDrawable(resources, R.drawable.ic_chevron_right, null)
    }

    private fun setDrawables() {
        previousMonthButtonIcon?.let {
            prevMonthButton.setImageDrawable(it)
        }

        nextMonthButtonIcon?.let {
            nextMonthButton.setImageDrawable(it)
        }
    }

    private fun setColors() {
        titleColor?.let {
            titleText.setTextColor(it)
        }

        dateRangeTextColor?.let {
            dateRangeText.setTextColor(it)
        }

        daysOfWeekTextColor?.let {
            daysOfWeekLinearLayout.forEach { view ->
                val tv = view as TextView
                tv.setTextColor(it)
            }
        }

        previousMonthButtonColor?.let {
            prevMonthButton.backgroundTintList = ColorStateList.valueOf(it)
        }

        nextMonthButtonColor?.let {
            nextMonthButton.backgroundTintList = ColorStateList.valueOf(it)
        }

        yearSelectButtonColor?.let {
            yearButton.backgroundTintList = ColorStateList.valueOf(it)
        }

        yearSelectButtonTextColor?.let {
            yearButton.setTextColor(it)
        }
    }

    fun initCalendar() {
        yearButton.text =
            "${CalendarUtils.Constants.INT_MONTHS_TO_STRING_MONTHS[currentMonth]} $selectedYear"
        setDateRangeText()

        val startDay = CalendarUtils.getDayOfWeekOfDate(selectedYear, currentMonth, 1)
        val daysInMonth = CalendarUtils.getDaysInMonth(currentMonth, selectedYear)

        daysInMonth?.let {
            textViews.clear()
            for (i in startDay until daysInMonth + startDay) {
                val tv = createTextView(i, startDay)
                textViews.add(tv)
            }

            tvsWithEvent.clear()
            textViews.forEach { tv ->
                calendarEvents.forEach {
                    checkIfDateHasEvent(tv, it)
                }
            }

            prevMonthButton.setOnClickListener {
                goToPreviousMonth()
            }

            nextMonthButton.setOnClickListener {
                goToNextMonth()
            }

            yearButton.setOnClickListener {
                switchCalendarViews()
            }

            drawSelectedDates()
        }
    }

    private fun checkIfDateHasEvent(
        tv: TextView,
        it: Pair<String, Calendar>
    ) {
        val calendarDate =
            sdf.parse("${currentMonth.plus(1)}/${tv.text.toString().toInt()}/${selectedYear}")
        val eventDate =
            sdf.parse("${it.second.timeInMillis.toLocalDateTime()?.month?.value.toString()}/${it.second.timeInMillis.toLocalDateTime()?.dayOfMonth.toString()}/${it.second.timeInMillis.toLocalDateTime()?.year.toString()}")

        calendarDate?.let {
            eventDate?.let {
                if (calendarDate == eventDate) {
                    setEventDot(tv)
                }
            }
        }
    }

    private fun setEventDot(tv: TextView) {
        val layers = ContextCompat.getDrawable(
            context,
            R.drawable.event_background
        ) as LayerDrawable?

        layers?.let {
            eventDotColor?.let { color ->
                it.getDrawable(it.findIndexByLayerId(R.id.eventDot)).setTint(color)
            }
        }

        tv.background = layers
        tvsWithEvent.add(tv.text.toString())
    }

    private fun createTextView(i: Int, startDay: Int): TextView {
        val day = i + 1
        val id: Int = resources.getIdentifier("d$day", "id", context.packageName)
        val tv = findViewById<TextView>(id).apply {
            text = (day - startDay).toString()
            tag = (day - startDay).toString()
            dayTextColor?.let {
                setTextColor(it)
            }
            visibility = VISIBLE
            setOnClickListener {
                if (firstSelectedDate["year"] != null && secondSelectedDate["year"] != null) {
                    setFirstSelectedDate(this)
                    secondSelectedDate = mutableMapOf()
                } else if (firstSelectedDate["year"] != null) {
                    setSecondSelectedDate(this)

                    val firstDate = getFirstSelectedDate()
                    val secondDate = getSecondSelectedDate()

                    firstDate?.let {
                        secondDate?.let {
                            if (firstDate == secondDate) {
                                clearSecondSelectedDate()
                            } else if (secondDate.before(firstDate)) {
                                setFirstSelectedDate(this)
                                secondSelectedDate = mutableMapOf()
                            }
                        }
                    }
                } else {
                    setFirstSelectedDate(this)
                }
                resetCalendar()
                initCalendar()
                drawSelectedDates()
            }
        }
        return tv
    }

    private fun setDateRangeText() {
        if (firstSelectedDate["year"] != null) {
            val month = "${
                if (firstSelectedDate["month"]!!.plus(1) < 10) "0${
                    firstSelectedDate["month"]!!.plus(
                        1
                    )
                }" else firstSelectedDate["month"]!!.plus(1)
            }"
            val day =
                "${if (firstSelectedDate["day"]!! < 10) "0${firstSelectedDate["day"]}" else firstSelectedDate["day"]}"
            val year = "${firstSelectedDate["year"]}"
            dateRangeText.text = "$month/$day/$year - mm/dd/yy"
        }
        if (firstSelectedDate["year"] != null && secondSelectedDate["year"] != null) {
            val firstMonth = "${
                if (firstSelectedDate["month"]!!.plus(1) < 10) "0${
                    firstSelectedDate["month"]!!.plus(
                        1
                    )
                }" else firstSelectedDate["month"]!!.plus(1)
            }"
            val firstDay =
                "${if (firstSelectedDate["day"]!! < 10) "0${firstSelectedDate["day"]}" else firstSelectedDate["day"]}"
            val firstYear = "${firstSelectedDate["year"]}"
            val secondMonth = "${
                if (secondSelectedDate["month"]!!.plus(1) < 10) "0${
                    secondSelectedDate["month"]!!.plus(
                        1
                    )
                }" else secondSelectedDate["month"]!!.plus(1)
            }"
            val secondDay =
                "${if (secondSelectedDate["day"]!! < 10) "0${secondSelectedDate["day"]}" else secondSelectedDate["day"]}"
            val secondYear = "${secondSelectedDate["year"]}"
            dateRangeText.text =
                "$firstMonth/$firstDay/$firstYear - $secondMonth/$secondDay/$secondYear"
        }
    }

    private fun goToNextMonth() {
        if (currentMonth in 0..10) {
            currentMonth += 1
        } else if (currentMonth == 11) {
            currentMonth = 0
        }
        resetCalendar()
        initCalendar()
    }

    private fun goToPreviousMonth() {
        if (currentMonth in 1..11) {
            currentMonth -= 1
        } else if (currentMonth == 0) {
            currentMonth = 11
        }
        resetCalendar()
        initCalendar()
    }

    private fun switchCalendarViews() {
        daysOfWeekLinearLayout.visibility =
            when (daysOfWeekLinearLayout.visibility) {
                View.VISIBLE -> View.GONE
                else -> View.VISIBLE
            }
        yearLinearLayout.visibility =
            when (yearLinearLayout.visibility) {
                View.VISIBLE -> View.GONE
                else -> View.VISIBLE
            }
        daysLinearLayout.visibility =
            when (daysLinearLayout.visibility) {
                View.VISIBLE -> View.GONE
                else -> View.VISIBLE
            }
    }

    private fun setFirstSelectedDate(tv: TextView) {
        firstSelectedDate["year"] = selectedYear
        firstSelectedDate["month"] = currentMonth
        firstSelectedDate["day"] = tv.text.toString().toInt()
    }

    private fun clearFirstSelectedDate() {
        firstSelectedDate["year"] = null
        firstSelectedDate["month"] = null
        firstSelectedDate["day"] = null
    }

    private fun getFirstSelectedDate(): Date? {
        return try {
            sdf.parse("${firstSelectedDate["month"]?.plus(1)}/${firstSelectedDate["day"]}/${firstSelectedDate["year"]}")
        } catch (e: Exception) {
            null
        }
    }

    private fun setSecondSelectedDate(tv: TextView) {
        secondSelectedDate["year"] = selectedYear
        secondSelectedDate["month"] = currentMonth
        secondSelectedDate["day"] = tv.text.toString().toInt()
    }

    private fun getSecondSelectedDate(): Date? {
        return try {
            sdf.parse("${secondSelectedDate["month"]?.plus(1)}/${secondSelectedDate["day"]}/${secondSelectedDate["year"]}")
        } catch (e: Exception) {
            null
        }
    }

    private fun clearSecondSelectedDate() {
        secondSelectedDate["year"] = null
        secondSelectedDate["month"] = null
        secondSelectedDate["day"] = null
    }

    private fun drawSelectedDates() {
        Log.i(TAG, "first $firstSelectedDate")
        Log.i(TAG, "second $secondSelectedDate")
        if (firstSelectedDate["year"] != null) {
            if (selectedYear == firstSelectedDate["year"] && currentMonth == firstSelectedDate["month"]) {
                val tv: TextView =
                    daysLinearLayout.findViewWithTag(firstSelectedDate["day"].toString())
                firstSelectedDateTextColor?.let {
                    tv.setTextColor(it)
                }

                val layers = if (tvsWithEvent.contains(tv.text.toString())) {
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.circle_event
                    ) as LayerDrawable?
                } else {
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.circle
                    ) as LayerDrawable?
                }

                layers?.let {
                    selectedDateCircleColor?.let { color ->
                        it.getDrawable(it.findIndexByLayerId(R.id.circle)).setTint(color)
                    }

                    if (tvsWithEvent.contains(tv.text.toString())) {
                        eventDotColor?.let { color ->
                            it.getDrawable(it.findIndexByLayerId(R.id.eventDot)).setTint(color)
                        }
                        eventDotColorWhenSelected?.let { color ->
                            it.getDrawable(it.findIndexByLayerId(R.id.eventDot)).setTint(color)
                        }
                    }
                }

                tv.background = layers

                if (secondSelectedDate["year"] != null) {
                    val layers = if (tvsWithEvent.contains(tv.text.toString())) {
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.circle_right_event
                        ) as LayerDrawable?
                    } else {
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.circle_right
                        ) as LayerDrawable?
                    }


                    layers?.let {
                        highlightedDatesBackgroundColor?.let { color ->
                            it.getDrawable(it.findIndexByLayerId(R.id.background)).setTint(color)
                        }
                        selectedDateCircleColor?.let { color ->
                            it.getDrawable(it.findIndexByLayerId(R.id.circle)).setTint(color)
                        }

                        if (tvsWithEvent.contains(tv.text.toString())) {
                            eventDotColor?.let { color ->
                                it.getDrawable(it.findIndexByLayerId(R.id.eventDot)).setTint(color)
                            }
                            eventDotColorWhenSelected?.let { color ->
                                it.getDrawable(it.findIndexByLayerId(R.id.eventDot)).setTint(color)
                            }
                        }
                    }

                    tv.background = layers
                }
            }
        }

        if (secondSelectedDate["year"] != null) {
            if (selectedYear == secondSelectedDate["year"] && currentMonth == secondSelectedDate["month"]) {
                val tv: TextView =
                    daysLinearLayout.findViewWithTag(secondSelectedDate["day"].toString())
                secondSelectedDateTextColor?.let {
                    tv.setTextColor(it)
                }

                val layers = if (tvsWithEvent.contains(tv.text.toString())) {
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.circle_event
                    ) as LayerDrawable?
                } else {
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.circle
                    ) as LayerDrawable?
                }

                layers?.let {
                    selectedDateCircleColor?.let { color ->
                        it.getDrawable(it.findIndexByLayerId(R.id.circle)).setTint(color)
                    }

                    if (tvsWithEvent.contains(tv.text.toString())) {
                        eventDotColor?.let { color ->
                            it.getDrawable(it.findIndexByLayerId(R.id.eventDot)).setTint(color)
                        }
                        eventDotColorWhenSelected?.let { color ->
                            it.getDrawable(it.findIndexByLayerId(R.id.eventDot)).setTint(color)
                        }
                    }
                }

                tv.background = layers


                if (firstSelectedDate["year"] != null) {
                    val layers = if (tvsWithEvent.contains(tv.text.toString())) {
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.circle_left_event
                        ) as LayerDrawable?
                    } else {
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.circle_left
                        ) as LayerDrawable?
                    }


                    layers?.let {
                        highlightedDatesBackgroundColor?.let { color ->
                            it.getDrawable(it.findIndexByLayerId(R.id.background)).setTint(color)
                        }
                        selectedDateCircleColor?.let { color ->
                            it.getDrawable(it.findIndexByLayerId(R.id.circle)).setTint(color)
                        }

                        if (tvsWithEvent.contains(tv.text.toString())) {
                            eventDotColor?.let { color ->
                                it.getDrawable(it.findIndexByLayerId(R.id.eventDot)).setTint(color)
                            }
                            eventDotColorWhenSelected?.let { color ->
                                it.getDrawable(it.findIndexByLayerId(R.id.eventDot)).setTint(color)
                            }
                        }
                    }

                    tv.background = layers
                }
            }
        }

        if (firstSelectedDate["year"] != null && secondSelectedDate["year"] != null) {
            val firstDate = getFirstSelectedDate()
            val secondDate = getSecondSelectedDate()
            for (i in 1 until 43) {
                val resID = resources.getIdentifier("d${i}", "id", context.packageName)
                val tv = findViewById<TextView>(resID)
                if (tv.text.toString() != "99") {
                    if (tv.text.toString().toInt() in 1..31 && tv.visibility == View.VISIBLE) {
                        val tvDate = sdf.parse(
                            "${currentMonth.plus(1)}/${
                                if (tv.text.toString().toInt() < 10) "0${
                                    tv.text.toString().toInt()
                                }" else tv.text.toString().toInt()
                            }/$selectedYear"
                        )
                        if (tvDate!!.after(firstDate) && tvDate.before(secondDate)) {
                            updateTextView(i)
                        }
                    }
                }
            }
        }
    }

    private fun updateTextView(i: Int) {
        val resID = resources.getIdentifier("d${i}", "id", context.packageName)
        val tv = findViewById<TextView>(resID)
        val layers = if (tvsWithEvent.contains(tv.text.toString())) {
            ContextCompat.getDrawable(context, R.drawable.selected_date_background_event) as LayerDrawable?
        } else {
            ContextCompat.getDrawable(context, R.drawable.selected_date_background) as LayerDrawable?
        }

        layers?.let {
            highlightedDatesBackgroundColor?.let { color ->
                it.getDrawable(it.findIndexByLayerId(R.id.background)).setTint(color)
            }
            if (tvsWithEvent.contains(tv.text.toString())) {
                eventDotColor?.let { color ->
                    it.getDrawable(it.findIndexByLayerId(R.id.eventDot)).setTint(color)
                }
                eventDotColorWhenHighlighted?.let { color ->
                    it.getDrawable(it.findIndexByLayerId(R.id.eventDot)).setTint(color)
                }
            }
        }

        tv?.apply {
            background = layers
            highlightedDatesTextColor?.let {
                setTextColor(it)
            }
        }
    }

    private fun resetCalendar() {
        for (i in 1 until 43) {
            val id: Int = resources.getIdentifier("d$i", "id", context.packageName)
            findViewById<TextView>(id).apply {
                text = context.getString(R.string.empty_date_text)
                visibility = View.INVISIBLE
                tag = null
                background = null
            }

        }
    }

    private fun initYearsCalendar() {
        val startYear = currentYear - 13
        var lastDrawnYear = startYear
        yearLinearLayout.forEach { linearLayout ->
            val ll = linearLayout as LinearLayout
            val end = if (lastDrawnYear + 4 < currentYear) lastDrawnYear + 4 else currentYear + 3
            for (i in lastDrawnYear until end) {
                val tvLayoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                ).apply {
                    weight = 1f
                }
                TextView(context).apply {
                    layoutParams = tvLayoutParams
                    text = "$i"
                    textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                    textSize = 17f
                    setTypeface(typeface, Typeface.BOLD)
                    yearTextColor?.let {
                        setTextColor(it)
                    }

                    if (i > currentYear) {
                        visibility = View.INVISIBLE
                    } else if (i == selectedYear) {
                        paintFlags = Paint.UNDERLINE_TEXT_FLAG
                    }
                    setOnClickListener {
                        selectedYear = (it as TextView).text.toString().toInt()
                        resetYearsCalendar()
                        initYearsCalendar()
                        resetCalendar()
                        initCalendar()
                        switchCalendarViews()
                    }
                    ll.addView(this)
                }
            }
            lastDrawnYear = end
        }
    }

    private fun resetYearsCalendar() {
        yearLinearLayout.forEach { view ->
            val ll = view as LinearLayout
            ll.removeAllViews()
        }
    }

    private fun isValidated(fromDate: String, toDate: String): Pair<Boolean, Pair<Long, Long>> {
        Log.i(TAG, "$fromDate $toDate")
        val firstMillis: Long
        val secondMillis: Long
        if (fromDate == "mm/dd/yyyy" && toDate == "mm/dd/yyyy") {
            return Pair(true, Pair(0L, 0L))
        } else {
            if (fromDate != "mm/dd/yyyy") {
                if (fromDate.length == 10) {
                    try {
                        val localDate = LocalDate.parse(
                            fromDate,
                            DateTimeFormatter.ofPattern("MM/dd/yyyy")
                        )
                        val localDateTime = localDate.atStartOfDay()
                        val date = ZonedDateTime.of(localDateTime, ZoneId.systemDefault())
                        val millis = date.toInstant().toEpochMilli()
                        firstMillis = millis
                    } catch (e: DateTimeParseException) {
                        Log.e(TAG, e.localizedMessage ?: "error" + "/n" + e.stackTrace.toString())
                        // false
                        Toast.makeText(context, "Could not parse start date", Toast.LENGTH_LONG)
                            .show()
                        return Pair(false, Pair(0L, 0L))
                    }

                } else {
                    // false
                    Toast.makeText(context, "Not a valid start date", Toast.LENGTH_LONG).show()
                    return Pair(false, Pair(0L, 0L))
                }
            } else {
                // false
                Toast.makeText(context, "Enter a start date", Toast.LENGTH_LONG).show()
                return Pair(false, Pair(0L, 0L))
            }

            if (toDate != "mm/dd/yyyy") {
                if (toDate.length == 10) {
                    return try {
                        val localDate = LocalDate.parse(
                            toDate,
                            DateTimeFormatter.ofPattern("MM/dd/yyyy")
                        )
                        val localDateTime = localDate.atStartOfDay()
                        val date = ZonedDateTime.of(localDateTime, ZoneId.systemDefault())
                        val millis = date.toInstant().toEpochMilli()
                        secondMillis = millis

                        Log.i(TAG, Pair(firstMillis, secondMillis).toString())
                        Pair(true, Pair(firstMillis, secondMillis))
                    } catch (e: DateTimeParseException) {
                        Log.e(TAG, e.localizedMessage ?: "error" + "/n" + e.stackTrace.toString())
                        // false
                        Toast.makeText(context, "Could not parse end date", Toast.LENGTH_LONG)
                            .show()
                        Pair(false, Pair(0L, 0L))
                    }

                } else {
                    // false
                    Toast.makeText(context, "Not a valid end date", Toast.LENGTH_LONG).show()
                    return Pair(false, Pair(0L, 0L))
                }
            } else {
                // false
                Toast.makeText(context, "Enter an end date", Toast.LENGTH_LONG).show()
                return Pair(false, Pair(0L, 0L))
            }
        }
    }
}

fun Long.toLocalDateTime(): LocalDateTime? {
    return try {
        val instant: Instant = Instant.ofEpochMilli(this)
        instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
    } catch (e: Exception) {
        Log.e("toLocalDateTime", e.stackTraceToString())
        null
    }
}