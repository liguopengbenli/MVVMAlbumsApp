package com.lig.intermediate.mvvmalbumsapp.features.album

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lig.intermediate.mvvmalbumsapp.data.AlbumsRepository
import com.lig.intermediate.mvvmalbumsapp.data.Annonce
import com.lig.intermediate.mvvmalbumsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumsRepository
) : ViewModel() {

    private val eventChannel = Channel<Event>()
    val events = eventChannel.receiveAsFlow()

    private val refreshTriggerChannel = Channel<Refresh>()
    private val refreshTrigger = refreshTriggerChannel.receiveAsFlow()

    var pendingScrollingToTopAfterRefresh = false

    init {
        viewModelScope.launch {
            refreshTriggerChannel.send(Refresh.FORCE)
        }
    }


    val albums = refreshTrigger.flatMapLatest { refresh->
        repository.getAlbums(
            onFetchSuccess = {
                // Scroll to top when success
                pendingScrollingToTopAfterRefresh = true
            },
            onFetchFailed = { t->
                viewModelScope.launch {
                    eventChannel.send(Event.ShowErrorMessage(t))
                }
            },
            forceRefresh = refresh == Refresh.FORCE
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)
    //convert to hot state flow, it can still be collected when user change fragment


    fun onManualRefresh() {
        if (albums.value !is Resource.Loading) {
            viewModelScope.launch {
                refreshTriggerChannel.send(Refresh.FORCE)
            }
        }
    }

    sealed class Event{
        data class ShowErrorMessage(val error: Throwable): Event()
    }

    enum class Refresh {
        FORCE, NORMAL
    }

    companion object{
        private const val TAG = "AlbumViewModel"
    }
}
