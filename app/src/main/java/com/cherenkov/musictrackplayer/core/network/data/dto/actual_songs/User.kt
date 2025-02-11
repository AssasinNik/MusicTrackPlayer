package com.cherenkov.musictrackplayer.core.network.data.dto.actual_songs

data class User(
    val id: Long,
    val name: String,
    val tracklist: String,
    val type: String
)