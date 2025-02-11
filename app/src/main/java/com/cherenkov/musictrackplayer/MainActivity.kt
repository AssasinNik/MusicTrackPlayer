package com.cherenkov.musictrackplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cherenkov.musictrackplayer.ui.theme.MusicTrackPlayerTheme
import com.cherenkov.musictrackplayer.features.api_tracks.presentation.TracksApiScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicTrackPlayerTheme {
                TracksApiScreen()
            }
        }
    }
}
