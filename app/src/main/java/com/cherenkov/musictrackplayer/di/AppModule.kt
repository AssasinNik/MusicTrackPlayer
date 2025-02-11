package com.cherenkov.musictrackplayer.di

import com.cherenkov.musictrackplayer.core.network.RemoteMusicDataSource
import com.cherenkov.musictrackplayer.core.network.RemoteMusicDataSourceImpl
import com.cherenkov.musictrackplayer.core.network.data.MusicApi
import com.cherenkov.musictrackplayer.features.api_tracks.data.DefaultMusicChartsRepository
import com.cherenkov.musictrackplayer.features.api_tracks.domain.repository.MusicChartsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://api.deezer.com"
    private const val TIMEOUT = 20L

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
    @Provides
    @Singleton
    fun provideApi(okHttpClient: OkHttpClient): MusicApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(MusicApi::class.java)
    }
    @Provides
    @Singleton
    fun provideMusicDataSource(api: MusicApi): RemoteMusicDataSource {
        return RemoteMusicDataSourceImpl(api)
    }
    @Provides
    @Singleton
    fun provideMusicRepository(remoteMusicDataSource: RemoteMusicDataSource): MusicChartsRepository {
        return DefaultMusicChartsRepository(remoteMusicDataSource)
    }
}