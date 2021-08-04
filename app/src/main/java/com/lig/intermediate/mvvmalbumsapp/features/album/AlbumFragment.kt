package com.lig.intermediate.mvvmalbumsapp.features.album

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lig.intermediate.mvvmalbumsapp.R
import com.lig.intermediate.mvvmalbumsapp.databinding.FragmentAlbumsBinding
import com.lig.intermediate.mvvmalbumsapp.util.Resource
import com.lig.intermediate.mvvmalbumsapp.util.exhaustive
import com.lig.intermediate.mvvmalbumsapp.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AlbumFragment : Fragment(R.layout.fragment_albums) {
    private val viewModel: AlbumViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAlbumsBinding.bind(view)

        val albumsAdapter = AlbumsAdapter(
            onItemClick = {
                val action = AlbumFragmentDirections.actionAlbumFragmentToDetailsFragment(it)
                findNavController().navigate(action)
            },
            onBookmarkClick = { annonce ->
                viewModel.onBookmarkClick(annonce)
            }
        )

        albumsAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY


        binding.apply {
            recyclerView.apply {
                adapter = albumsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                itemAnimator?.changeDuration = 0 //remove the effet click

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
                        result.error?.localizedMessage ?: getString(R.string.unknown_error_occurred)
                    )
                    albumsAdapter.submitList(result.data)
                }
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.onManualRefresh()
            }

            buttonRetry.setOnClickListener {
                viewModel.onManualRefresh()
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.events.collect { event ->
                    when (event) {
                        is AlbumViewModel.Event.ShowErrorMessage ->
                            showSnackbar(
                                getString(
                                    R.string.could_not_refresh,
                                    event.error.localizedMessage
                                        ?: getString(R.string.unknown_error_occurred)
                                )
                            )
                    }.exhaustive // turn it to expression
                }
            }
        }
        setHasOptionsMenu(true)


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_album, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_refresh -> {
            viewModel.onManualRefresh()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "AlbumFragment"
    }

}