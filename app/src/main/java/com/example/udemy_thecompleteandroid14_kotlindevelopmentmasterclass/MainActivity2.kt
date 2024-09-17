package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.ageinminutescalculator.MainAgeCalculatorActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.calculatorapp.MainCalculatorActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.activities.SplashScreenActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.ui.theme.Udemy_TheCompleteAndroid14_KotlinDevelopmentMasterclassTheme


class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Udemy_TheCompleteAndroid14_KotlinDevelopmentMasterclassTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    SetButtonsForMainActivity2()
                }
            }
        }
    }
}

@Composable
fun SetButtonsForMainActivity2(){
    val context = LocalContext.current
    val buttonShape = RoundedCornerShape(
        topStart = 2.dp,
        topEnd = 2.dp,
        bottomStart = 2.dp,
        bottomEnd = 2.dp
    )
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Color.White,
        contentColor = Color.Black
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            val openCalculatorApp = Intent(context, MainCalculatorActivity::class.java)
            context.startActivity(openCalculatorApp)
        }, colors = buttonColors, shape = buttonShape, modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Calculator", fontSize = 14.sp)
        }
        
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = {
            val openAgeInMinutesApp = Intent(context, MainAgeCalculatorActivity::class.java)
            context.startActivity(openAgeInMinutesApp)
        }, colors = buttonColors, shape = buttonShape, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Age In Minutes Calculator")
        }
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = { 
            val openTrelloApp = Intent(context, SplashScreenActivity::class.java)
            context.startActivity(openTrelloApp)
        }, colors = buttonColors, shape = buttonShape, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Trello App")
        }
    }
}

@Preview
@Composable
fun MainActivity2Preview(){
    SetButtonsForMainActivity2()
}