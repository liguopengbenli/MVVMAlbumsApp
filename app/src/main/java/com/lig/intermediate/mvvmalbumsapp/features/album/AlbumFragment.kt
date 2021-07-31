package com.lig.intermediate.mvvmalbumsapp.features.album

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lig.intermediate.mvvmalbumsapp.R
import com.lig.intermediate.mvvmalbumsapp.databinding.FragmentAlbumsBinding
import com.lig.intermediate.mvvmalbumsapp.shared.AlbumsAdapter
import com.lig.intermediate.mvvmalbumsapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AlbumFragment : Fragment(R.layout.fragment_albums) {
    private val viewModel: AlbumViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAlbumsBinding.bind(view)
        val albumsAdapter = AlbumsAdapter()


        binding.apply {
            recyclerView.apply {
                adapter = albumsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.albums.collect {
                    val result = it ?: return@collect
                    swipeRefreshLayout.isRefreshing = result is Resource.Loading
                    recyclerView.isVisible = !result.data.isNullOrEmpty()
                    textViewError.isVisible = result.error != null && result.data.isNullOrEmpty()
                    buttonRetry.isVisible = result.error != null && result.data.isNullOrEmpty()
                    textViewError.text = getString(
                        R.string.could_not_refresh,
                        result.error?.localizedMessage?:getString(R.string.unknown_error_occurred)
                    )
                    albumsAdapter.submitList(result.data)
                }
            }

        }


    }

    companion object {
        private const val TAG = "AlbumFragment"
    }

}