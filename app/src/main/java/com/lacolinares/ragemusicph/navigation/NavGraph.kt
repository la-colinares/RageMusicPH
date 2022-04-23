package com.lacolinares.ragemusicph.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.lacolinares.ragemusicph.extensions.customComposable
import com.lacolinares.ragemusicph.presentation.ui.screens.MainScreen
import com.lacolinares.ragemusicph.presentation.ui.screens.SplashScreen


@ExperimentalAnimationApi
@Composable
fun NavGraph() {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ) {
        customComposable(Screen.SplashScreen.route){
            SplashScreen(navController = navController)
        }
        customComposable(Screen.MainScreen.route){
            MainScreen()
        }
    }
}