package com.cherenkov.musictrackplayer.core.network

import com.cherenkov.musictrackplayer.core.network.data.dto.actual_songs.ActualSongsDTO
import com.nikitacherenkov.musicplayer.core_network.domain.DataError
import com.nikitacherenkov.musicplayer.core_network.domain.Result

interface RemoteMusicDataSource {

    suspend fun getChartSongs(): Result<ActualSongsDTO, DataError.Remote>

}