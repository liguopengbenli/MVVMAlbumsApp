package com.lig.mvvmalbumsapp.features.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lig.mvvmalbumsapp.data.AlbumsRepository
import com.lig.mvvmalbumsapp.data.Annonce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val repository: AlbumsRepository
): ViewModel() {

    val bookmarks = repository.getAllBookMarkedAnnonces()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun onBookmarkClick(annonce: Annonce) {
        val currentBookmarked = annonce.isBookMarked
        val updateAnnonce = annonce.copy(isBookMarked = !currentBookmarked)
        viewModelScope.launch {
            repository.updateAnnonce(updateAnnonce)
        }
    }

    fun onDeleteAllBookmarks() {
        viewModelScope.launch {
            repository.resetAllBookMarks()
        }
    }

}