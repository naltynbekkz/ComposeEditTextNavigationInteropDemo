package com.example.composeedittextnavigationinteropdemo

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
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
                        val context = LocalContext.current
                        val view = remember(context) {
                            val view = EditText(context)
                            view.setText("step 2. focus this editText")
                            view
                        }
                        AndroidView(
                            factory = { view },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        val lifecycleOwner = LocalLifecycleOwner.current

                        DisposableEffect(lifecycleOwner) {
                            val observer = LifecycleEventObserver { _, event ->
                                when (event) {
                                    Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP -> {
                                        view.clearFocus()
                                        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                                            .hideSoftInputFromWindow(view.windowToken, 0)
                                    }

                                    else -> {}
                                }
                            }
                            lifecycleOwner.lifecycle.addObserver(observer)
                            onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
                        }

                        Button(onClick = { navController.navigateUp() }) {
                            Text("step 3: click this button to navigate up")
                        }
                    }
                }
            }
        }
    }
}