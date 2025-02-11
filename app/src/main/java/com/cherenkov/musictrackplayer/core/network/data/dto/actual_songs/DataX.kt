package com.cherenkov.musictrackplayer.core.network.data.dto.actual_songs

import com.cherenkov.musictrackplayer.core.network.data.dto.actual_songs.User

data class DataX(
    val checksum: String,
    val creation_date: String,
    val id: Long,
    val link: String,
    val md5_image: String,
    val nb_tracks: Int,
    val picture: String,
    val picture_big: String,
    val picture_medium: String,
    val picture_small: String,
    val picture_type: String,
    val picture_xl: String,
    val `public`: Boolean,
    val title: String,
    val tracklist: String,
    val type: String,
    val user: User
)