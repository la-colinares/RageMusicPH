package com.lacolinares.ragemusicph.presentation.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lacolinares.ragemusicph.R
import com.lacolinares.ragemusicph.custom.Space
import com.lacolinares.ragemusicph.navigation.Screen
import com.lacolinares.ragemusicph.presentation.MainViewModel
import com.lacolinares.ragemusicph.presentation.ui.theme.Background
import com.lacolinares.ragemusicph.presentation.ui.theme.Red
import com.lacolinares.ragemusicph.presentation.ui.theme.orbitronFamily

@Composable
fun HomeScreen(navController: NavController, viewModel: MainViewModel) {
    val context = LocalContext.current
    val musicCategories = viewModel.musicCategories
    Column(
        modifier = Modifier
            .padding(start = 12.dp, top = 48.dp, end = 12.dp, bottom = 24.dp)
            .fillMaxSize()
    ) {
        Header(
            title = context.getString(R.string.company_name),
            textAlignment = TextAlign.Center,
            textSize = 24.sp
        )
        Space(40)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ){
            items(items = musicCategories){ category ->
                MusicCard(title = category.title, description = category.description) {
                    viewModel.setIsHome(false)
                    viewModel.setMusicCategory(category)
                    navController.navigate(Screen.MusicPlayerScreen.route)
                }
            }
        }
    }
}

@Composable
private fun MusicCard(title: String, description: String, onClick: () -> Unit){
    val gradient = Brush.horizontalGradient(0f to Red, 1000f to Background)
    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.clickable { 
            onClick.invoke()
        }
    ) {
        Column(
            modifier = Modifier
                .background(gradient)
                .padding(16.dp)
        ) {
            Header(title = title, textSize = 18.sp)
            Space()
            Header(title = description, textSize = 12.sp)
        }
    }
}

@Composable
private fun Header(
    title: String = "",
    textAlignment: TextAlign = TextAlign.Left,
    textSize: TextUnit = 32.sp,
) {
    Text(
        text = title,
        color = Color.White,
        fontFamily = orbitronFamily,
        fontWeight = FontWeight.Medium,
        fontSize = textSize,
        textAlign = textAlignment,
        modifier = Modifier.fillMaxWidth()
    )
}