package com.lig.mvvmalbumsapp.features.album

import android.os.Bundle
import android.text.InputType.TYPE_CLASS_NUMBER
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lig.mvvmalbumsapp.R
import com.lig.mvvmalbumsapp.databinding.FragmentAlbumsBinding
import com.lig.mvvmalbumsapp.shared.AlbumsAdapter
import com.lig.mvvmalbumsapp.util.Resource
import com.lig.mvvmalbumsapp.util.exhaustive
import com.lig.mvvmalbumsapp.util.onQueryTextSubmit
import com.lig.mvvmalbumsapp.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AlbumFragment : Fragment(R.layout.fragment_albums) {
    private val viewModel: AlbumViewModel by viewModels()
    private lateinit var searchView: SearchView
    private var tagglePage = true


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

        albumsAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        binding.apply {
            progressBarAlbum.isVisible = true
            recyclerView.apply {
                adapter = albumsAdapter
                layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
                setHasFixedSize(true)
                itemAnimator?.changeDuration = ANIMATION_DURATION //remove the effet click
                // Add pagination manuel using scrolling and album Id
                val scrollListener = object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val lastVisibleItemPosition =
                            (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                        val totalItemCount = recyclerView?.layoutManager?.itemCount
                        if (totalItemCount == lastVisibleItemPosition + 1 && tagglePage) {
                            Log.d(TAG, "lig onScroll!!!!")
                            //recyclerView.removeOnScrollListener(this)
                            tagglePage = false
                            viewModel.nextPage()
                        }
                    }
                }
                addOnScrollListener(scrollListener)
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.albums.collect {
                    val result = it ?: return@collect
                    swipeRefreshLayout.isRefreshing = result is Resource.Loading
                    recyclerView.isVisible = !result.data.isNullOrEmpty()
                    textViewNoResults.isVisible =
                        result.data.isNullOrEmpty() && result.error == null && !swipeRefreshLayout.isRefreshing
                    textViewError.isVisible = result.error != null && result.data.isNullOrEmpty()
                    buttonRetry.isVisible = result.error != null && result.data.isNullOrEmpty()
                    progressBarAlbum.isVisible =
                        !recyclerView.isVisible && !swipeRefreshLayout.isRefreshing
                                && !textViewNoResults.isVisible && !textViewError.isVisible
                    textViewError.text = getString(
                        R.string.could_not_refresh,
                        result.error?.localizedMessage ?: getString(R.string.unknown_error_occurred)
                    )
                    albumsAdapter.submitList(result.data) {
                        tagglePage = true
                        if (viewModel.pendingScrollingToTopAfterRefresh) {
                            recyclerView.scrollToPosition(INIT_POSITION)
                            viewModel.pendingScrollingToTopAfterRefresh = false
                        }
                    }
                }
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.onManualRefresh()
                viewModel.previousPage()
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
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView.inputType = TYPE_CLASS_NUMBER

        searchView.queryHint = getString(R.string.search_hint)

        searchView.onQueryTextSubmit { query ->
            viewModel.onSearchQuerySubmit(query)
            viewModel.pendingScrollingToTopAfterRefresh = true
            searchView.clearFocus()
        }
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

private const val ANIMATION_DURATION = 0L
private const val SPAN_COUNT = 2
private const val INIT_POSITION = 0
