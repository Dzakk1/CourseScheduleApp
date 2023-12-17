package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener{

    private lateinit var viewModel : AddCourseViewModel

    private var startTime = ""
    private var endTime = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        val viewModewlFactory = AddCourseViewModelFactory.createFactory(this)

        viewModel = ViewModelProvider(this, viewModewlFactory) [AddCourseViewModel::class.java]

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_insert -> {
                val courseName = findViewById<TextInputEditText>(R.id.ed_course_name).text.toString()
                val spinnerDay = findViewById<Spinner>(R.id.spinner_day).selectedItem.toString()
                val spinnerDayNumber = getDayNumberByDayName(spinnerDay)
                val lecturer = findViewById<TextInputEditText>(R.id.ed_lecturer).text.toString()
                val note = findViewById<TextInputEditText>(R.id.ed_note).text.toString()

                when {
                    courseName.isEmpty() -> false
                    startTime.isEmpty() -> false
                    endTime.isEmpty() -> false
                    spinnerDayNumber == -1 -> false
                    lecturer.isEmpty() -> false
                    note.isEmpty() -> false
                    else -> {
                        viewModel.insertCourse(courseName,spinnerDayNumber,startTime,endTime,lecturer,note)
                        finish()
                        true
                    }
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getDayNumberByDayName(dayName: String): Int {
        val day = resources.getStringArray(R.array.day)
        return day.indexOf(dayName)
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {

        val setCalendar = Calendar.getInstance()
        val setTime = SimpleDateFormat("HH:mm", Locale.getDefault())

        setCalendar.set(Calendar.MINUTE, minute)
        setCalendar.set(Calendar.HOUR_OF_DAY, hour)

        when (tag) {
            "start_time" -> {
                findViewById<TextView>(R.id.tv_start_time).text = setTime.format(setCalendar.time)
                startTime = setTime.format(setCalendar.time)
            }
            "end_time" -> {
                findViewById<TextView>(R.id.tv_end_time).text = setTime.format(setCalendar.time)
                endTime = setTime.format(setCalendar.time)
            }
        }

    }

    fun setTimePicker(view: View) {
        val tag = when (view.id) {
            R.id.ib_start_time -> "start_time"
            R.id.ib_end_time -> "end_time"
            else -> "default"
        }

        val fragmentPicker = TimePickerFragment()
        fragmentPicker.show(supportFragmentManager, tag)
    }
}