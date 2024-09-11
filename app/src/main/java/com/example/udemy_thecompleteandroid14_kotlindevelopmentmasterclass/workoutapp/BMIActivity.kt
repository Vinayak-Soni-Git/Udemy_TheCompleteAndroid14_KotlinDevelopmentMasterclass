package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.workoutapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    
    companion object{
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
        private const val US_UNITS_VIEW = "US_UNIT_VIEW"
    }
    private var currentVisibleView:String = METRIC_UNITS_VIEW
    
    private var binding:ActivityBmiBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBmiActivity)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Calculate BMI"
        }
        binding?.toolbarBmiActivity?.setNavigationOnClickListener{
            onBackPressed()
        }
        makeVisibleMetricUnitsView()
        
        binding?.unitsRG?.setOnCheckedChangeListener{ _, checkedId:Int ->
            if(checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }else{
                makeVisibleUSUnitsView()
            }
            
        }
        
        binding?.calculateUnitsBtn?.setOnClickListener{
            calculateUnits()
        }
    }
    
    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.metricUnitWeightTIL?.visibility = View.VISIBLE
        binding?.metricUnitHeightTIL?.visibility = View.VISIBLE
        binding?.usMetricUnitWeightTIL?.visibility = View.GONE
        binding?.metricUsUnitHeightFeetTIL?.visibility = View.GONE
        binding?.metricUsUnitHeightInchTIL?.visibility = View.GONE
        
        binding?.metricUnitHeightET?.text!!.clear()
        binding?.metricUnitWeightET?.text!!.clear()
        
        binding?.displayBMIResultLL?.visibility = View.INVISIBLE
    }

    private fun makeVisibleUSUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        binding?.metricUnitWeightTIL?.visibility = View.INVISIBLE
        binding?.metricUnitHeightTIL?.visibility = View.INVISIBLE
        binding?.usMetricUnitWeightTIL?.visibility = View.VISIBLE
        binding?.metricUsUnitHeightFeetTIL?.visibility = View.VISIBLE
        binding?.metricUsUnitHeightInchTIL?.visibility = View.VISIBLE

        binding?.usMetricUnitHeightFeetET?.text!!.clear()
        binding?.usMetricUnitWeightET?.text!!.clear()
        binding?.usMetricUnitHeightInchET?.text!!.clear()

        binding?.displayBMIResultLL?.visibility = View.INVISIBLE
    }
    
    private fun displayBMIResult(bmi:Float){
        val bmiLabel:String
        val bmiDescription:String
        
        if(bmi.compareTo(15f) <= 0){
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat More!"
        }
        else if(bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0){
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat More!"
        }
        else if(bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0){
            bmiLabel = "underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat More!"
        }
        else if(bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0){
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! you are in a good shape!"
        }
        else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()
        
        binding?.displayBMIResultLL?.visibility = View.VISIBLE
        binding?.bmiValueTV?.text = bmiValue
        binding?.bmiTypeTV?.text = bmiLabel
        binding?.bmiDescriptionTV?.text = bmiDescription
    }
    
    private fun validateMetricUnits():Boolean{
        var isValid = true
        if(binding?.metricUnitWeightET?.text.toString().isEmpty()){
            isValid = false
        }else if(binding?.metricUnitHeightET?.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }
    
    private fun calculateUnits(){
        if(currentVisibleView == METRIC_UNITS_VIEW){
            if(validateMetricUnits()){
                val heightValue:Float = binding?.metricUnitHeightET?.text.toString().toFloat()/100
                val weightValue:Float = binding?.metricUnitWeightET?.text.toString().toFloat()
                val bmi = weightValue/(heightValue*heightValue)
                displayBMIResult(bmi)
            }else{
                Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_SHORT).show()

            }
        }else{
            if(validateUsUnits()){
                val usUnitHeightValueFeet:String = binding?.usMetricUnitHeightFeetET?.text.toString()
                val usUnitHeightValueInch:String = binding?.usMetricUnitHeightInchET?.text.toString()
                val usUnitWeightValue:Float = binding?.usMetricUnitWeightET?.text.toString().toFloat()
                val heightValue = usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12
                val bmi = 703*(usUnitWeightValue/(heightValue*heightValue))
            }else{
                Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateUsUnits():Boolean{
        var isValid = true
        when{
            binding?.usMetricUnitWeightET?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.usMetricUnitHeightFeetET?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.usMetricUnitHeightInchET?.text.toString().isEmpty() -> {
                isValid = false
            }
        }
        return isValid
    }
}