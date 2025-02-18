package com.cherenkov.musictrackplayer.core.network

import com.cherenkov.musictrackplayer.core.network.data.MusicApi
import com.cherenkov.musictrackplayer.core.network.data.dto.actual_songs.ActualSongsDTO
import com.cherenkov.musictrackplayer.core.network.data.dto.searched_songs.SearchedSongsDTO
import com.cherenkov.musictrackplayer.core.network.data.safeCall
import com.nikitacherenkov.musicplayer.core_network.domain.DataError
import com.nikitacherenkov.musicplayer.core_network.domain.Result
import javax.inject.Inject


class RemoteMusicDataSourceImpl @Inject constructor(
    private val api: MusicApi
) : RemoteMusicDataSource {
    override suspend fun getChartSongs(): Result<ActualSongsDTO, DataError.Remote> {
        return safeCall {
            api.getActualSongs()
        }
    }

    override suspend fun getFindingSongs(text: String): Result<SearchedSongsDTO, DataError.Remote> {
        return safeCall {
            api.getSearchedSongs(text)
        }
    }
}