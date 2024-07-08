package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.ui.theme.Udemy_TheCompleteAndroid14_KotlinDevelopmentMasterclassTheme

class NavigationApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Udemy_TheCompleteAndroid14_KotlinDevelopmentMasterclassTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavApp()
                }
            }
        }
    }
}

@Composable
fun NavApp(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "firstscreen"){
        composable(route = "firstscreen"){
            FirstScreen { name ->
                navController.navigate("secondscreen/$name")
            }
        }
        composable(route = "secondscreen/{name}"){
            val name = it.arguments?.getString("name") ?: "no name"
            SecondScreen(name) {
                navController.navigate("firstscreen")
            }
        }
        
    }
}