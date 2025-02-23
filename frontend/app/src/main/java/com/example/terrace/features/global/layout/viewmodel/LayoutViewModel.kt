package com.example.terrace.features.global.layout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LayoutViewModel : ViewModel() {

    private val _navigationEvent = MutableLiveData<NavigationAction?>()
    val navigationEvent: LiveData<NavigationAction?> = _navigationEvent

    fun onStatsClick() {
        _navigationEvent.value = NavigationAction.Stats
    }

    fun onHomeClick() {
        _navigationEvent.value = NavigationAction.Home
    }

    fun onLeaderboardClick() {
        _navigationEvent.value = NavigationAction.Leaderboard
    }

    fun onPrevConstellation() {
        _navigationEvent.value = NavigationAction.Previous
    }

    fun onNextConstellation() {
        _navigationEvent.value = NavigationAction.Next
    }

    fun resetNavigationEvent() {
        _navigationEvent.value = null
    }
}

sealed class NavigationAction {
    object Stats : NavigationAction()
    object Home : NavigationAction()
    object Leaderboard : NavigationAction()
    object Previous : NavigationAction()
    object Next : NavigationAction()
}
