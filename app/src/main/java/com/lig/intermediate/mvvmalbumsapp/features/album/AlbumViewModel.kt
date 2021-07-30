package com.lig.intermediate.mvvmalbumsapp.features.album

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lig.intermediate.mvvmalbumsapp.data.AlbumsRepository
import com.lig.intermediate.mvvmalbumsapp.data.Annonce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumsRepository
) : ViewModel() {
    private val albumsFlow = MutableStateFlow<List<Annonce>>(emptyList())
    val albums: Flow<List<Annonce>> = albumsFlow

    init {
        viewModelScope.launch {
            Log.i(TAG, "lig launch search")
            val data = repository.getAlbums()
            albumsFlow.value = data
        }
    }

    companion object{
        private const val TAG = "AlbumViewModel"
    }
}
