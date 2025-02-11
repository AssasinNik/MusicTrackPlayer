package com.cherenkov.musictrackplayer.core.network.data.dto.actual_songs

import com.cherenkov.musictrackplayer.core.network.data.dto.actual_songs.Data

data class Artists(
    val `data`: List<Data>,
    val total: Int
)