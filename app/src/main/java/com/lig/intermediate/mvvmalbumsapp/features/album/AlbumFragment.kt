package com.lig.intermediate.mvvmalbumsapp.features.album

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lig.intermediate.mvvmalbumsapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumFragment: Fragment(R.layout.fragment_albums) {
    private val viewModel: AlbumViewModel by viewModels()


}