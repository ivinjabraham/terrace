package com.example.terrace.core.network.models


data class ScoreResponse(
    val username: String,
    val score: Int,
    val createdAt: String,
    val screentime: Long,
)