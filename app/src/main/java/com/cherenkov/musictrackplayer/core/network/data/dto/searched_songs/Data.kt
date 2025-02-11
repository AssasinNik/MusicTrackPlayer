package com.cherenkov.musictrackplayer.core.network.data.dto.searched_songs

import com.cherenkov.musictrackplayer.core.network.data.dto.searched_songs.Album
import com.cherenkov.musictrackplayer.core.network.data.dto.searched_songs.Artist

data class Data(
    val album: Album,
    val artist: Artist,
    val duration: Int,
    val explicit_content_cover: Int,
    val explicit_content_lyrics: Int,
    val explicit_lyrics: Boolean,
    val id: Long,
    val link: String,
    val md5_image: String,
    val preview: String,
    val rank: Int,
    val readable: Boolean,
    val title: String,
    val title_short: String,
    val title_version: String,
    val type: String
)