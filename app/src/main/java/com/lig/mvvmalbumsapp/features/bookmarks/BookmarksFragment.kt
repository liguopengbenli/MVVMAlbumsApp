package com.lig.mvvmalbumsapp.features.bookmarks

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
import com.lig.mvvmalbumsapp.R
import com.lig.mvvmalbumsapp.databinding.FragmentBookmarksBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class BookmarksFragment : Fragment(R.layout.fragment_bookmarks) {

    private val viewModel: BookmarksViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentBookmarksBinding.bind(view)

        val bookmarksAdapter = BookmarksAdapter(
            onItemClick = {
                val action =
                    BookmarksFragmentDirections.actionBookmarksFragmentToDetailsFragment(it)
                findNavController().navigate(action)
            },
            onBookmarkClick = { annonce ->
                viewModel.onBookmarkClick(annonce)
            }
        )

        bookmarksAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY


        binding.apply {
            recyclerView.apply {
                adapter = bookmarksAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.bookmarks.collect {
                    val bookmarks = it ?: return@collect
                    bookmarksAdapter.submitList(bookmarks)
                    textViewNoBookmarks.isVisible = bookmarks.isEmpty()
                    recyclerView.isVisible = bookmarks.isNotEmpty()
                }
            }
        }

        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_bookmarks, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_delete_all_bookmarks -> {
                viewModel.onDeleteAllBookmarks()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

}