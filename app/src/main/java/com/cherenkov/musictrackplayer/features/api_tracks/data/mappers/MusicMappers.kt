package com.cherenkov.musictrackplayer.features.api_tracks.data.mappers

import com.cherenkov.musictrackplayer.core.network.data.dto.actual_songs.DataXXX
import com.cherenkov.musictrackplayer.core.network.data.dto.searched_songs.Data
import com.cherenkov.musictrackplayer.features.api_tracks.domain.model.Items

fun DataXXX.toItem(): Items {
    return Items(
        id = id,
        cover = album.cover,
        title = title,
        artist_name = artist.name
    )
}

fun Data.toItem(): Items {
    return Items(
        id = id,
        cover = album.cover,
        title = title,
        artist_name = artist.name
    )
}