package com.example.terrace.core.network.models

data class LeaderboardResponse(
    val username: String,
    val score: Int,
    val rank: Int,
    val isCurrentUser: Boolean,
)