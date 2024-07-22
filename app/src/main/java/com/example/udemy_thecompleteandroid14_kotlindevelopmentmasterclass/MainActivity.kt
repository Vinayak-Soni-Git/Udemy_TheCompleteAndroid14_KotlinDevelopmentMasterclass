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
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.drawingapp.MainDrawingActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.locationapp.MainLocationApp
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.musicapp.MainMusicApp
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.mvvmarchitecture.MVVMCounterApp
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.navigation.NavigationApp
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.quizapp.MainQuizActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.recipeapp.MainRecipeApp
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.shoppinglistapp.ShoppingListApp
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.unitconverterapp.UnitConverterApp
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.ui.theme.Udemy_TheCompleteAndroid14_KotlinDevelopmentMasterclassTheme
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.wishlistapp.MainWishListApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Udemy_TheCompleteAndroid14_KotlinDevelopmentMasterclassTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                   SetButtons()
                }
            }
        }
    }
}
@Composable
fun SetButtons(){
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
            val openUnitConverterApp = Intent(context, UnitConverterApp::class.java)
            context.startActivity(openUnitConverterApp)
        }, colors = buttonColors, shape = buttonShape, modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Unit Converter App", fontSize = 14.sp)
        }
        
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = {
            val openShoppingListApp = Intent(context, ShoppingListApp::class.java)
            context.startActivity(openShoppingListApp)
        }, colors = buttonColors, shape = buttonShape, modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Shopping List App", fontSize = 14.sp)
        }
        
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = {
            val openShoppingListApp = Intent(context, MVVMCounterApp::class.java)
            context.startActivity(openShoppingListApp)
        }, colors = buttonColors, shape = buttonShape, modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "MVVM Counter App", fontSize = 14.sp)
        }
        
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = {
            val openMainRecipeApp = Intent(context, MainRecipeApp::class.java)
            context.startActivity(openMainRecipeApp)
        }, colors = buttonColors, shape = buttonShape, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Recipe App", fontSize = 14.sp)
        }
        
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = {
            val openNavigationApp = Intent(context, NavigationApp::class.java)
            context.startActivity(openNavigationApp)
        }, colors = buttonColors, shape = buttonShape, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Navigation App", fontSize = 14.sp)
        }
        
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = { 
            val openLocationApp = Intent(context, MainLocationApp::class.java)
            context.startActivity(openLocationApp)
        }, colors = buttonColors, shape = buttonShape, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Location App", fontSize = 14.sp)
        }
        
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = { 
            val openWishListApp = Intent(context, MainWishListApp::class.java)
            context.startActivity(openWishListApp)
        }, colors = buttonColors, shape = buttonShape, modifier = Modifier.fillMaxWidth()) {
            Text(text = "WishList App", fontSize = 14.sp)
        }
        
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = {
            val openMusicApp = Intent(context, MainMusicApp::class.java)
            context.startActivity(openMusicApp)
        }, colors = buttonColors, shape = buttonShape, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Music App", fontSize = 14.sp)
        }
        
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = { 
            val openQuizApp = Intent(context, MainQuizActivity::class.java)
            context.startActivity(openQuizApp)
        }, colors = buttonColors, shape = buttonShape, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Quiz App", fontSize = 14.sp)
        }
        
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = {
            val openDrawingApp = Intent(context, MainDrawingActivity::class.java)
            context.startActivity(openDrawingApp)
        }, colors = buttonColors, shape = buttonShape, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Drawing App", fontSize = 14.sp)
        }
    }
}

@Preview
@Composable
fun MainActivityPreview(){
    SetButtons()
}