package com.lacolinares.ragemusicph.navigation

sealed class Screen(val route: String){
    object SplashScreen: Screen("splash")
    object MainScreen: Screen("main")
}
