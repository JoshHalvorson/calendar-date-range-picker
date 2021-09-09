package dev.joshhalvorson.calendar

import android.graphics.Color
import android.icu.text.DateFormat
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.joshhalvorson.calendar.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventsList = listOf(
            Pair("event  1", Calendar.getInstance()),
            Pair("event  2", Calendar.Builder().setDate(2021, 8, 12).build()),
            Pair("event  3", Calendar.Builder().setDate(2020, 8, 11).build()),
            Pair("event  4", Calendar.Builder().setDate(2021, 2, 1).build()),
            Pair("event  5", Calendar.Builder().setDate(2021, 11, 8).build()),
        )
        binding.calendarPicker.addEvents(eventsList)

        binding.calendarPicker.eventDotColor = Color.CYAN
        binding.calendarPicker.eventDotColorWhenSelected = Color.RED
        binding.calendarPicker.eventDotColorWhenHighlighted = Color.GREEN
        binding.calendarPicker.setFirstSelectedDate(year = 2021, month = 8, day = 9)
        binding.calendarPicker.setSecondSelectedDate(year = 2021, month = 8, day = 19)
        
        binding.calendarPicker.initCalendar()

        binding.getDateRangeButton.setOnClickListener {
            val selectedDates = binding.calendarPicker.getSelectedDates()

            if (selectedDates != null) {
                val firstDate = DateFormat.getDateInstance().format(Date(selectedDates.first))
                val secondDate = DateFormat.getDateInstance().format(Date(selectedDates.second))
                Toast.makeText(
                    applicationContext,
                    "Date range from $firstDate to $secondDate",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}