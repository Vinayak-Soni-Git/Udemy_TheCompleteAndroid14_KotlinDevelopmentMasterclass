package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.musicapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SubscriptionView(){
    Column(
        modifier = Modifier.height(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Manager Subscription")
        Card(modifier = Modifier.padding(8.dp), elevation = 4.dp) {
            Column(modifier = Modifier.padding(8.dp)) {
                Column {
                    Text(text = "Musical")
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Free Tier")
                        TextButton(onClick = { /*TODO*/ }) {
                            Row {
                                Text(text = "See All Plans")
                                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "See All Plans")
                            }
                        }
                    }
                }
                Divider(thickness = 1.dp, modifier = Modifier.padding(horizontal = 8.dp))
                Row(Modifier.padding(vertical = 16.dp)) {
                    Icon(imageVector = Icons.Default.AccountBox, contentDescription = "Get a Plan")
                    Text(text = "Get a plan")
                }
            }
            
        }
    }
}