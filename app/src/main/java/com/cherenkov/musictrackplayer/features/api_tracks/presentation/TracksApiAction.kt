package com.cherenkov.musictrackplayer.features.api_tracks.presentation

sealed interface TracksApiAction {
    data class OnTrackClicked(val id: String): TracksApiAction
}