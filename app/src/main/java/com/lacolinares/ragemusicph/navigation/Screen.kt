package com.lacolinares.ragemusicph.navigation

sealed class Screen(val route: String){
    object MainScreen: Screen("main")
    object MusicPlayerScreen: Screen("music-player")
}
