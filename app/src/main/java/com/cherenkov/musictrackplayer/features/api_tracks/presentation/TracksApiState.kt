package com.cherenkov.musictrackplayer.features.api_tracks.presentation

import com.cherenkov.musictrackplayer.core.ui.UiText
import com.cherenkov.musictrackplayer.features.api_tracks.domain.model.Items

data class TracksApiState(
    val charts: List<Items> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null
)