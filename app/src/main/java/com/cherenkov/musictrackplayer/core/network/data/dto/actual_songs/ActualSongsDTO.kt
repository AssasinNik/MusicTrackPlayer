package com.cherenkov.musictrackplayer.core.network.data.dto.actual_songs

import com.cherenkov.musictrackplayer.core.network.data.dto.actual_songs.Albums
import com.cherenkov.musictrackplayer.core.network.data.dto.actual_songs.Artists
import com.cherenkov.musictrackplayer.core.network.data.dto.actual_songs.Playlists
import com.cherenkov.musictrackplayer.core.network.data.dto.actual_songs.Podcasts
import com.cherenkov.musictrackplayer.core.network.data.dto.actual_songs.Tracks

data class ActualSongsDTO(
    val albums: Albums,
    val artists: Artists,
    val playlists: Playlists,
    val podcasts: Podcasts,
    val tracks: Tracks
)