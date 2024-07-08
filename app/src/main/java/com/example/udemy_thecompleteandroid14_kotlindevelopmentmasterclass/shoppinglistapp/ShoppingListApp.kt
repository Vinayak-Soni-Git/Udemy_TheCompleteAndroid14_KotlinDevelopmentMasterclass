package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.shoppinglistapp

import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.ui.theme.Udemy_TheCompleteAndroid14_KotlinDevelopmentMasterclassTheme

class ShoppingListApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Udemy_TheCompleteAndroid14_KotlinDevelopmentMasterclassTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation(){
    val navController = rememberNavController()
    val viewModel:LocationViewModel = viewModel()
    val context = LocalContext.current
    val shoppingLocationUtils = ShoppingLocationUtils(context)
    
    NavHost(navController = navController, startDestination = "shoppinglistscreen") {
        composable("shoppinglistscreen"){
            ShoppingListApplication(
                shoppingLocationUtils = shoppingLocationUtils,
                viewModel = viewModel,
                navController = navController,
                context = context,
                address = viewModel.address.value.firstOrNull()?.formattedAddress ?: "No Address"
            )
        }
        dialog("locationscreen"){backstack ->
            viewModel.location.value?.let { 
                LocationSelectionScreen(location = it, onLocationSelected = {
                    viewModel.fetchAddress("${it.latitude}, ${it.longitude}")
                    navController.popBackStack()
                })
            }
            
        }
    }
}




@Preview
@Composable
fun ShoppingListApplicationPreview(){
//    ShoppingListApplication()
}
