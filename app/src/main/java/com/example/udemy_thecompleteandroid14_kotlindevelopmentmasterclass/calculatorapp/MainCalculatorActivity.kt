package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.calculatorapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R

class MainCalculatorActivity : AppCompatActivity() {
    
    private var inputTV:TextView? = null
    var lastNumeric:Boolean = false
    var lastDot:Boolean = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_calculator)
        inputTV = findViewById(R.id.inputTV)
    }
    fun onDigit(view:View){
        inputTV?.append((view as Button).text)
        lastNumeric = true
        lastDot = false
    }
    fun onClear(view:View){
        inputTV?.text = ""
    }
    fun onDecimalPoint(view:View){
        if(lastNumeric && !lastDot){
            inputTV?.append(".")
            lastNumeric = false
            lastDot = true
        }
    }
    
    fun onOperator(view:View){
        inputTV?.text?.let {
            if(lastNumeric && isOperatorAdded(it.toString())){
                inputTV?.append((view as Button).text)  
                lastNumeric = false
                lastDot = false
            }
        }
    }
    
    private fun onEqual(view:View){
        if(lastNumeric){
            var tvValue = inputTV?.text.toString()
            var prefix = ""
            
            try{
                if(tvValue.startsWith("-")){
                    prefix = "-"
                    tvValue = tvValue.substring(1)
                }
                if(tvValue.contains("-")){
                    val splitValue = tvValue.split("-")
                    var one = splitValue[0]
                    var two = splitValue[1]
                    
                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }

                    inputTV?.text = removeZeroAfterDot((one.toDouble()-two.toDouble()).toString())
                    
                }else if(tvValue.contains("+")){
                    val splitValue = tvValue.split("+")
                    var one = splitValue[0]
                    var two = splitValue[1]

                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }
                    inputTV?.text = removeZeroAfterDot((one.toDouble()+two.toDouble()).toString())
                    
                }else if(tvValue.contains("/")){
                    val splitValue = tvValue.split("/")
                    var one = splitValue[0]
                    var two = splitValue[1]

                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }
                    inputTV?.text = removeZeroAfterDot((one.toDouble()/two.toDouble()).toString())
                    
                }else if(tvValue.contains("*")){
                    val splitValue = tvValue.split("*")
                    var one = splitValue[0]
                    var two = splitValue[1]

                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }
                    
                    inputTV?.text = removeZeroAfterDot((one.toDouble()*two.toDouble()).toString())
                }
                
            }catch(e:ArithmeticException){
                e.printStackTrace()
            }
        }
    }
    private fun removeZeroAfterDot(result:String):String{
        var value = result
        if(result.contains(".0")){
            value = result.substring(0, result.length-2)
        }
        return value
    }
    
    private fun isOperatorAdded(value:String):Boolean{
        return if(value.startsWith("-")){
            false
        }else{
            value.contains("/")
                    || value.contains("*")
                    || value.contains("+")
                    || value.contains("-")                    
        }
    }
}