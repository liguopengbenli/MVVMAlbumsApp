package com.lig.intermediate.mvvmalbumsapp.features.album

import androidx.lifecycle.ViewModel
import com.lig.intermediate.mvvmalbumsapp.data.AlbumsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumsRepository
) : ViewModel() {
}
