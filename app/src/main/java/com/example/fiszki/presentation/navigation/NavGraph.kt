package com.example.fiszki.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fiszki.presentation.screens.ReviewScreen.ReviewScreen
import com.example.fiszki.presentation.screens.addCardScreen.AddCardScreen
import com.example.fiszki.presentation.screens.homeScreen.HomeScreen
import com.example.fiszki.presentation.screens.settingsScreen.SettingsScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(
                modifier = Modifier,
                onReviewClick = { navController.navigate(Screen.ReviewScreen.route) },
                onCreateCardClick = { navController.navigate(Screen.AddCard.route) },
               onSettingClick = {navController.navigate(Screen.SettingsScreen.route)},
                onPracticeClick = {}
            )
        }
        composable(Screen.AddCard.route) {
            AddCardScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.ReviewScreen.route) {
            ReviewScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.SettingsScreen.route){
            SettingsScreen(onBackClick = {navController.popBackStack()})
        }

    }
}


sealed class Screen(val route: String) {

    data object HomeScreen : Screen("home_screen")

    data object AddCard : Screen("add_card")

    data object ReviewScreen : Screen("review_screen")

    data object SettingsScreen : Screen("settingsScreen")
}