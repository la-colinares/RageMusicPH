package com.lacolinares.ragemusicph.presentation.ui.screens.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainScreenViewModel : ViewModel() {

    private val _rebindService = MutableStateFlow(false)
    val rebindService : StateFlow<Boolean> = _rebindService

    private val _foregroundServiceStopped = MutableStateFlow(false)
    val foregroundServiceStopped : StateFlow<Boolean> = _foregroundServiceStopped

    fun setRebindService(rebind: Boolean){
        _rebindService.value = rebind
    }

    fun setForegroundServiceStopped(stop: Boolean){
        _foregroundServiceStopped.value = stop
    }
}