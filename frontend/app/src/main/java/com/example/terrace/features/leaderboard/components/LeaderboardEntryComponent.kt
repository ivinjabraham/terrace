package com.example.terrace.features.home.components

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val points: Int,
    val isCurrentUser: Boolean = false
)
