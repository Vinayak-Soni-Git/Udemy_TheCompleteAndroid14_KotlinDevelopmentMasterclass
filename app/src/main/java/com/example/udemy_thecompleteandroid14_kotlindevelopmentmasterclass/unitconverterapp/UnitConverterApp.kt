package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.unitconverterapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.ui.theme.Udemy_TheCompleteAndroid14_KotlinDevelopmentMasterclassTheme
import kotlin.math.roundToInt

class UnitConverterApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Udemy_TheCompleteAndroid14_KotlinDevelopmentMasterclassTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    UnitConverter()
                }
            }
        }
    }
}

@Composable
fun UnitConverter(){
    
    var inputValue by remember { mutableStateOf("") }
    var outputValue by remember { mutableStateOf("") }
    var inputUnit by remember { mutableStateOf("Meters") }
    var outputUnit by remember { mutableStateOf("Meters") }
    var iExpended by remember { mutableStateOf(false) }
    var oExpended by remember { mutableStateOf(false) }
    val conversionFactor = remember { mutableStateOf(1.00) }
    val oConversionFactor = remember { mutableStateOf(1.00) }
    
    fun convertUnits(){
        val inputValueDouble = inputValue.toDoubleOrNull() ?: 0.0
        val result = (inputValueDouble * conversionFactor.value * 100.0/ oConversionFactor.value).roundToInt()/100.0
        outputValue = result.toString()
        
    }
    
   Column (modifier = Modifier.fillMaxSize(),
           verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally){
       Spacer(modifier = Modifier.height(20.dp))
       
       Text(text = "Unit Converter", 
           color = Color.White,
           fontFamily = FontFamily.Monospace,
           fontSize = 24.sp
       )
       Spacer(modifier = Modifier.height(16.dp))
       OutlinedTextField(
           value = inputValue,
           keyboardOptions = KeyboardOptions.Default.copy(
               keyboardType = KeyboardType.Number
           ),
           onValueChange = {
           inputValue = it
           convertUnits()    
       }, label = {Text("Enter Value")})
       Spacer(modifier = Modifier.height(16.dp))
       Row{
           //Input box
           Box {
               Button(onClick = { iExpended = true }) {
                   Text(text = inputUnit)
                   Icon(Icons.Default.ArrowDropDown, contentDescription = "")
               }    
               DropdownMenu(expanded = iExpended, onDismissRequest = { iExpended = false}) {
                   DropdownMenuItem(text = { Text("Centimeters") }, onClick = { 
                       iExpended = false
                       inputUnit = "Centimeters"
                       conversionFactor.value = 0.01
                       convertUnits()
                   })
                   DropdownMenuItem(text = {Text("Meters") }, onClick = {
                       iExpended = false
                       inputUnit = "Meters"
                       conversionFactor.value = 1.0
                       convertUnits()
                   })
                   DropdownMenuItem(text = { Text("Feet") }, onClick = {
                       iExpended = false
                       inputUnit = "Feet"
                       conversionFactor.value = 0.3048
                       convertUnits()
                   })
                   DropdownMenuItem(text = { Text("Millimeters") }, onClick = {
                       iExpended = false
                       inputUnit = "Millimeters"
                       conversionFactor.value = 0.001
                       convertUnits()   
                   })
               }
           }
           Spacer(modifier = Modifier.width(16.dp))
           // Output box
           Box {
               Button(onClick = { oExpended = true }) {
                   Text(text = outputUnit)
                   Icon(Icons.Default.ArrowDropDown, contentDescription = "")
               }
               DropdownMenu(expanded = oExpended, onDismissRequest = { oExpended = false}) {
                   DropdownMenuItem(text = { Text("Centimeters") }, onClick = {
                       oExpended = false
                       outputUnit = "Centimeters"
                       oConversionFactor.value = 0.01
                       convertUnits()
                   })
                   DropdownMenuItem(text = {Text("Meters") }, onClick = {
                       oExpended = false
                       outputUnit = "Meters"
                       oConversionFactor.value = 1.00
                       convertUnits()
                   })
                   DropdownMenuItem(text = { Text("Feet") }, onClick = {
                       oExpended = false
                       outputUnit = "Feet"
                       oConversionFactor.value = 0.3048
                       convertUnits()
                   })
                   DropdownMenuItem(text = { Text("Millimeters") }, onClick = {
                       oExpended = false
                       outputUnit = "Millimeters"
                       oConversionFactor.value = 0.001
                       convertUnits()
                   })
               }
           }
           
       }
       Spacer(modifier = Modifier.height(16.dp))
       Text(text = "Result: $outputValue $outputUnit", 
           color = Color.White, 
           style = MaterialTheme.typography.headlineSmall
       )
   } 
    
}

@Preview
@Composable
fun UnitConverterPreview(){
    UnitConverter()
}