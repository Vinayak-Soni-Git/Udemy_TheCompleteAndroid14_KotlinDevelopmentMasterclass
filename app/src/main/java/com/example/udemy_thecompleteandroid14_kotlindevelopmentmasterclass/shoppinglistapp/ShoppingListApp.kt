package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.shoppinglistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.shoppinglistapp.ui.theme.Udemy_TheCompleteAndroid14_KotlinDevelopmentMasterclassTheme

class ShoppingListApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Udemy_TheCompleteAndroid14_KotlinDevelopmentMasterclassTheme {
                
            }
        }
    }
}
