package com.lacolinares.ragemusicph.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomScreen (val route: String, val title: String, val icon: ImageVector){
    object Home: BottomScreen("nav-home", "Home", Icons.Filled.Home)
    object About: BottomScreen("nav-about", "About", Icons.Filled.Info)
}

val bottomNavItems = listOf(
    BottomScreen.Home,
    BottomScreen.About,
)
