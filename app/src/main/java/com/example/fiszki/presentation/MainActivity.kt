package com.example.fiszki.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph
import com.example.fiszki.presentation.navigation.NavGraph
import com.example.fiszki.presentation.screens.ReviewScreen.ReviewScreen
import com.example.fiszki.presentation.screens.addCardScreen.AddCardCommand
import com.example.fiszki.presentation.screens.addCardScreen.AddCardScreen
import com.example.fiszki.presentation.screens.homeScreen.HomeScreen
import com.example.fiszki.presentation.ui.theme.FiszkiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FiszkiTheme {
                //AddCardScreen( {})
                //HomeScreen(Modifier,{},{},{},{})
               //ReviewScreen(Modifier, {},)
                NavGraph()
            }
        }
    }
}

