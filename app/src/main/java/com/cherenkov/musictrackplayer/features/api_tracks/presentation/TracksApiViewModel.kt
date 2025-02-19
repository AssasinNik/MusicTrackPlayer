package com.cherenkov.musictrackplayer.features.api_tracks.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cherenkov.musictrackplayer.core.ui.toUiText
import com.cherenkov.musictrackplayer.features.api_tracks.domain.repository.MusicChartsRepository
import com.nikitacherenkov.musicplayer.core_network.domain.onError
import com.nikitacherenkov.musicplayer.core_network.domain.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
    private var searchJob: Job? = null

    fun onAction(action: TracksApiAction) {
        when (action) {
            is TracksApiAction.OnTrackClicked -> {

            }
            is TracksApiAction.OnChangedText -> {
                findTracks(text = action.text)
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

    private fun findTracks(text: String){
        searchJob?.cancel()

        if (text != ""){
            searchJob = viewModelScope.launch {
                _state.update { it.copy(
                    isFinding = true
                ) }
                repository.findTracks(text)
                    .onSuccess { result ->
                        _state.update { it.copy(
                            isFinding = false,
                            errorMessage = null,
                            searchedSongs = result
                        ) }
                    }
                    .onError { error ->
                        _state.update { it.copy(
                            searchedSongs = emptyList(),
                            isLoading = false,
                            errorMessage = error.toUiText()
                        ) }
                    }
            }
        }

    }

}