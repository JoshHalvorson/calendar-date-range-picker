package dev.joshhalvorson.calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.joshhalvorson.calendar.ui.theme.CalendarTheme
import dev.joshhalvorson.calendar_date_range_picker.calendar.compose.CalendarPicker
import dev.joshhalvorson.calendar_date_range_picker.calendar.compose.CalendarRangePicker
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var selectedDate by rememberSaveable { mutableStateOf("") }
            var selectedDateRange by rememberSaveable { mutableStateOf("") }
            val formatter = DateTimeFormatter.ofPattern("EEEE MMMM d, yyyy")
            val minDate = LocalDate.now().toEpochDay()
            val maxDate = LocalDate.now().plusDays(7).toEpochDay()

            CalendarTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        CalendarPicker()


                        CalendarRangePicker()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    CalendarTheme {
        Greeting2("Android")
    }
}