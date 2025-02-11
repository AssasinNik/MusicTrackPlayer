package com.cherenkov.musictrackplayer.core.network.data

import com.cherenkov.musictrackplayer.core.network.data.dto.actual_songs.ActualSongsDTO
import com.cherenkov.musictrackplayer.core.network.data.dto.searched_songs.SearchedSongsDTO
import com.cherenkov.musictrackplayer.core.network.data.dto.song.SongDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MusicApi {

    @GET("/chart")
    suspend fun getActualSongs(): Response<ActualSongsDTO>

    @GET("/search")
    suspend fun getSearchedSongs(
        @Query("q") query: String
    ): Response<SearchedSongsDTO>

    @GET("/track/{id}")
    suspend fun getTrack(@Path("id") songId: String): Response<SongDTO>
}