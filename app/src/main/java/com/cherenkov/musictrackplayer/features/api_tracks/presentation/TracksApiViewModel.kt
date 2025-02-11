package com.cherenkov.musictrackplayer.features.api_tracks.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.cherenkov.musictrackplayer.core.ui.toUiText
import com.cherenkov.musictrackplayer.features.api_tracks.domain.repository.MusicChartsRepository
import com.nikitacherenkov.musicplayer.core_network.domain.onError
import com.nikitacherenkov.musicplayer.core_network.domain.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TracksApiViewModel @Inject constructor(
    private val repository: MusicChartsRepository
) : ViewModel(){


    private val _state = MutableStateFlow(TracksApiState())
    val state = _state
        .onStart {
            getCharts()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: TracksApiAction) {
        when (action) {
            is TracksApiAction.OnTrackClicked -> {

            }
        }
    }

    private fun getCharts() = viewModelScope.launch {
        _state.update { it.copy(
           isLoading = true
        ) }
        repository
            .searchCharts()
            .onSuccess { result ->
                _state.update { it.copy(
                    isLoading = false,
                    errorMessage = null,
                    charts = result
                ) }
            }
            .onError { error ->
                _state.update { it.copy(
                    charts = emptyList(),
                    isLoading = false,
                    errorMessage = error.toUiText()
                ) }
            }
    }
}