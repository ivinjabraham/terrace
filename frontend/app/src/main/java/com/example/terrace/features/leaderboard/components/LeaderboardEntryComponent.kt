package com.example.terrace.features.leaderboard

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val points: Int,
    val isCurrentUser: Boolean = false
)
