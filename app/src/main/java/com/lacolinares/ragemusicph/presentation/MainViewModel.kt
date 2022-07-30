package com.lacolinares.ragemusicph.presentation

import androidx.lifecycle.ViewModel
import com.lacolinares.ragemusicph.R
import com.lacolinares.ragemusicph.presentation.ui.screens.home.model.MusicCategory
import com.lacolinares.ragemusicph.utils.Constants.URL_EMOTIONS
import com.lacolinares.ragemusicph.utils.Constants.URL_HIPHOP_RNB
import com.lacolinares.ragemusicph.utils.Constants.URL_RAP_HITS
import com.lacolinares.ragemusicph.utils.Constants.URL_SCREAM
import com.lacolinares.ragemusicph.utils.Constants.URL_VIBE_RADIO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val _isHome = MutableStateFlow(true)
    val isHome: StateFlow<Boolean> = _isHome

    private val _foregroundServiceStopped = MutableStateFlow(false)
    val foregroundServiceStopped: StateFlow<Boolean> = _foregroundServiceStopped

    val musicCategories: List<MusicCategory> = generateMusicCategories()

    private val _selectedMusicCategory = MutableStateFlow(MusicCategory())
    val selectedMusicCategory: StateFlow<MusicCategory> = _selectedMusicCategory

    fun setMusicCategory(category: MusicCategory){
        _selectedMusicCategory.value = category
    }

    fun setIsHome(home: Boolean){
        _isHome.value = home
    }

    fun setForegroundServiceStopped(stop: Boolean) {
        _foregroundServiceStopped.value = stop
    }

    private fun generateMusicCategories(): List<MusicCategory> {
        return listOf(
            MusicCategory(
                title = "Rage Music PH",
                description = "100% Pinoy Rap Music",
                logo = R.drawable.ic_rage_music_ph,
                url = URL_RAP_HITS,
            ),
            MusicCategory(
                title = "Vibe Radio",
                description = "Today’s Best Music",
                logo = R.drawable.ic_vibe,
                url = URL_VIBE_RADIO,
            ),
            MusicCategory(
                title = "Emotions",
                description = "24/7 Lovesongs",
                logo = R.drawable.ic_emotions,
                url = URL_EMOTIONS,
            ),
            MusicCategory(
                title = "Hot Hiphop & RNB",
                description = "Fresh Urban Hits",
                logo = R.drawable.ic_hiphop_rnb,
                url = URL_HIPHOP_RNB,
            ),
            MusicCategory(
                title = "Scream Radio",
                description = "Best Rock Songs",
                logo = R.drawable.ic_scream,
                url = URL_SCREAM,
            ),
        )
    }
}