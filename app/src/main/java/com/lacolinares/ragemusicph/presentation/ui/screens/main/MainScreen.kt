package com.lacolinares.ragemusicph.presentation.ui.screens.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lacolinares.ragemusicph.extensions.customComposable
import com.lacolinares.ragemusicph.navigation.BottomScreen
import com.lacolinares.ragemusicph.navigation.Screen
import com.lacolinares.ragemusicph.navigation.bottomNavItems
import com.lacolinares.ragemusicph.presentation.MainViewModel
import com.lacolinares.ragemusicph.presentation.ui.screens.about.AboutScreen
import com.lacolinares.ragemusicph.presentation.ui.screens.home.HomeScreen
import com.lacolinares.ragemusicph.presentation.ui.theme.Background
import com.lacolinares.ragemusicph.presentation.ui.theme.BottomNavBackground

@ExperimentalAnimationApi
@Composable
fun MainScreen(mainNavController: NavController, viewModel: MainViewModel){
    val bottomNavController = rememberNavController()
    Scaffold(
        bottomBar = {
            AppBottomNav(
                mainNavController = mainNavController,
                navController = bottomNavController,
                viewModel = viewModel
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(Background)
                .fillMaxSize()
                .padding(innerPadding)
        ){
            BottomNavHost(
                mainNavController = mainNavController,
                bottomNavController = bottomNavController,
                viewModel = viewModel
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun BottomNavHost(mainNavController: NavController, bottomNavController: NavHostController, viewModel: MainViewModel){
    NavHost(navController = bottomNavController, startDestination = BottomScreen.Home.route){
        customComposable(BottomScreen.Home.route){
            HomeScreen(navController = mainNavController, viewModel = viewModel)
        }
        customComposable(BottomScreen.About.route){
            AboutScreen()
        }
    }
}

@Composable
private fun AppBottomNav(mainNavController: NavController, navController: NavController, viewModel: MainViewModel){
    val mainBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val bottomNavBackStackEntry by navController.currentBackStackEntryAsState()
    val mainScreenRoute = mainBackStackEntry?.destination?.route
    val currentNavRoute = bottomNavBackStackEntry?.destination?.route

    if (mainScreenRoute == Screen.MainScreen.route && currentNavRoute == BottomScreen.Home.route){
        viewModel.setIsHome(true)
    }

    NavigationBar(
        containerColor = BottomNavBackground,
        contentColor = Color.White,
        tonalElevation = 14.dp,
        modifier = Modifier.clip(shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)).height(64.dp)
    ) {
        bottomNavItems.forEach { nav ->
            NavigationBarItem(
                icon = { Icon(nav.icon, contentDescription = null) },
                label = { Text(text = nav.title) },
                selected = currentNavRoute == nav.route,
                alwaysShowLabel = currentNavRoute == nav.route,
                onClick = {
                    when(nav.route){
                        BottomScreen.Home.route -> {
                            viewModel.setIsHome(true)
                            navController.navigate(BottomScreen.Home.route){
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route){
                                        saveState = false
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        BottomScreen.About.route -> {
                            viewModel.setIsHome(false)
                            navController.navigate(BottomScreen.About.route){
                                launchSingleTop = false
                                restoreState = false
                            }
                        }
                    }
                }
            )
        }
    }
}