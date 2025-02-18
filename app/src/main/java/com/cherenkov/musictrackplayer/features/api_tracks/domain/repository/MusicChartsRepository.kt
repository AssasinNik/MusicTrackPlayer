package com.cherenkov.musictrackplayer.features.api_tracks.domain.repository

import com.cherenkov.musictrackplayer.features.api_tracks.domain.model.Items
import com.nikitacherenkov.musicplayer.core_network.domain.DataError
import com.nikitacherenkov.musicplayer.core_network.domain.Result

interface MusicChartsRepository {

    suspend fun searchCharts(): Result<List<Items>, DataError.Remote>

    suspend fun findTracks(text: String): Result<List<Items>, DataError.Remote>

}