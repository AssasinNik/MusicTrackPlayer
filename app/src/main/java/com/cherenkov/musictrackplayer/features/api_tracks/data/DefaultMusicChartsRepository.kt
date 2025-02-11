package com.cherenkov.musictrackplayer.features.api_tracks.data

import com.cherenkov.musictrackplayer.features.api_tracks.data.mappers.toItem
import com.cherenkov.musictrackplayer.core.network.RemoteMusicDataSource
import com.nikitacherenkov.musicplayer.core_network.domain.DataError
import com.nikitacherenkov.musicplayer.core_network.domain.Result
import com.nikitacherenkov.musicplayer.core_network.domain.map
import com.cherenkov.musictrackplayer.features.api_tracks.domain.model.Items
import com.cherenkov.musictrackplayer.features.api_tracks.domain.repository.MusicChartsRepository
import javax.inject.Inject

class DefaultMusicChartsRepository @Inject constructor(
    private val remoteMusicDataSource: RemoteMusicDataSource
): MusicChartsRepository {

    override suspend fun searchCharts(): Result<List<Items>, DataError.Remote> {
        return remoteMusicDataSource
            .getChartSongs()
            .map { dto ->
                dto.tracks.data.map { it.toItem() }
            }
    }

}