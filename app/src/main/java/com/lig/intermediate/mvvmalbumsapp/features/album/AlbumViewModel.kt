package com.lig.intermediate.mvvmalbumsapp.features.album

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lig.intermediate.mvvmalbumsapp.data.AlbumsRepository
import com.lig.intermediate.mvvmalbumsapp.data.Annonce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumsRepository
) : ViewModel() {
    val albums = repository.getAlbums()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    companion object{
        private const val TAG = "AlbumViewModel"
    }
}
