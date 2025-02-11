package com.cherenkov.musictrackplayer.core.network.data.dto.searched_songs

import com.cherenkov.musictrackplayer.core.network.data.dto.searched_songs.Data

data class SearchedSongsDTO(
    val `data`: List<Data>,
    val next: String,
    val total: Int
)