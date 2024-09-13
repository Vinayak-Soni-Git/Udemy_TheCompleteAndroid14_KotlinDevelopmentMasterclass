package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.ageinminutescalculator

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainAgeCalculatorActivity : AppCompatActivity() {
    
    private var tvSelectedDate:TextView? = null
    private var tvAgeInMinutes:TextView? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_age_calculator)
        
        tvAgeInMinutes = findViewById(R.id.tvAgeInMinutes)
        
        
        val btnDatePicker:Button = findViewById(R.id.btnDatePicker)
        btnDatePicker.setOnClickListener{
            clickDatePicker()
        }
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        
    }
    private fun clickDatePicker(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        
        val datePickerDialog = DatePickerDialog(this,
            {_, selectedYear, selectedMonth, selectedDayOfMonth -> 
                val selectedDate = "$selectedDayOfMonth/${selectedMonth+1}/$selectedYear"
                
                tvSelectedDate?.text = selectedDate
                
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                val theDate = simpleDateFormat.parse(selectedDate)
                theDate?.let {
                    val selectedDateInMinutes = theDate.time / 60000

                    val currentDate = simpleDateFormat.parse(simpleDateFormat.format(System.currentTimeMillis()))
                    currentDate?.let {
                        val currentDateInMinutes = currentDate.time / 60000
                        val differenceInMinutes = currentDateInMinutes - selectedDateInMinutes

                        tvAgeInMinutes?.text = differenceInMinutes.toString()
                    }
                    
                }
                
                
            },
            year,
            month,
            day)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 86400000
        datePickerDialog.show()
    }
}