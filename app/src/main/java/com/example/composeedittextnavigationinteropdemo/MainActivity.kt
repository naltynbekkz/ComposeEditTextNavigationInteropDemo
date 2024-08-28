package com.example.composeedittextnavigationinteropdemo

import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, "first") {
                composable("first") {
                    Column {
                        Button(onClick = { navController.navigate("second") }) {
                            Text("step 1. click this button to navigate to second screen")
                        }
                        Text("step 4. notice how the ime is still visible")
                    }
                }
                composable("second") {
                    Column {
                        AndroidView(
                            factory = {
                                val view = EditText(it)
                                view.setText("step 2. focus this editText")
                                view
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Button(onClick = { navController.navigateUp() }) {
                            Text("step 3: click this button to navigate up")
                        }
                    }
                }
            }
        }
    }
}