package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.meridian.data.jagi.JagiRepo
import com.example.meridian.ui.screens.jagi.JagiRootApp
import com.example.ui.theme.MeridianTheme

class MainActivity : ComponentActivity() {

    private val jagiRepo by lazy { JagiRepo() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeridianTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    JagiRootApp(repo = jagiRepo)
                }
            }
        }
    }
}
