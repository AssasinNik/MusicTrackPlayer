package com.cherenkov.musictrackplayer.features.api_tracks.presentation

import com.cherenkov.musictrackplayer.core.ui.UiText
import com.cherenkov.musictrackplayer.features.api_tracks.domain.model.Items

data class TracksApiState(
    val charts: List<Items> = emptyList(),
    val isLoading: Boolean = true,
    val isFinding: Boolean = false,
    val errorMessage: UiText? = null,
    val searchedSongs: List<Items> = emptyList()
)