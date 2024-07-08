package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SecondScreen(name:String, navigationToFirstScreen:(String) -> Unit){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "This is the second screen", fontSize = 24.sp, color = Color.White)
        Text(text = "Welcome $name", fontSize = 24.sp, color = Color.White)
        
        Button(onClick = { navigationToFirstScreen(name) }) {
            Text(text = "Go to first screen")
        }
    }
}

@Preview
@Composable
fun SecondScreenPreview(){
    SecondScreen("denis", {})
}