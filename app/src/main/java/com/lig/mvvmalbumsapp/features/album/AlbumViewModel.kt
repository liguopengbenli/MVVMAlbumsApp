package com.lig.mvvmalbumsapp.features.album

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.lig.mvvmalbumsapp.data.AlbumsRepository
import com.lig.mvvmalbumsapp.data.Annonce
import com.lig.mvvmalbumsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumsRepository,
    state: SavedStateHandle
) : ViewModel() {

    private val albumId = state.getLiveData<Int?>(ALBUM_ID_KEY, START_ALBUM_ID)
    private val eventChannel = Channel<Event>()
    val events = eventChannel.receiveAsFlow()
    var pendingScrollingToTopAfterRefresh = false


    private val refreshTriggerChannel = Channel<Refresh>()
    private val refreshTrigger = refreshTriggerChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            refreshTriggerChannel.send(Refresh.FORCE) // trigger the first page
        }
    }

    val albums = combine(refreshTrigger, albumId.asFlow()) { refresh, id ->
        Pair(refresh, id)
    }.flatMapLatest { (refresh, id) ->
        repository.getAlbums(
            albumId = id,
            onFetchSuccess = {},
            onFetchFailed = { t ->
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

    fun onBookmarkClick(annonce: Annonce) {
        val currentBookmarked = annonce.isBookMarked
        val updateAnnonce = annonce.copy(isBookMarked = !currentBookmarked)
        viewModelScope.launch {
            repository.updateAnnonce(updateAnnonce)
        }
    }

    fun onSearchQuerySubmit(query: Int) {
        albumId.value = query
    }

    fun nextPage() {
        if (albumId.value in START_ALBUM_ID until END_ALBUM_ID) {
            albumId.value = albumId.value?.plus(1)
        }
    }

    fun previousPage() {
        if (albumId.value in (START_ALBUM_ID + 1).rangeTo(END_ALBUM_ID)) {
            albumId.value = albumId.value?.minus(1)
        }
    }

    sealed class Event {
        data class ShowErrorMessage(val error: Throwable) : Event()
    }

    enum class Refresh {
        FORCE, NORMAL
    }

    companion object {
        private const val TAG = "AlbumViewModel"
    }
}

private const val START_ALBUM_ID = 1
private const val END_ALBUM_ID = 100
private const val ALBUM_ID_KEY = "currentid"
