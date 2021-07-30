package com.lig.intermediate.mvvmalbumsapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.lig.intermediate.mvvmalbumsapp.databinding.ActivityMainBinding
import com.lig.intermediate.mvvmalbumsapp.features.album.AlbumFragment
import com.lig.intermediate.mvvmalbumsapp.features.bookmarks.BookmarksFragment
import dagger.hilt.android.AndroidEntryPoint


/*
* Not use navigation component because:
* it will recreate fragment every time switch bottom nav bar
* */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var albumFragment: AlbumFragment
    private lateinit var bookmarksFragment: BookmarksFragment

    private val fragments: Array<Fragment>
        get() = arrayOf(
            albumFragment,
            bookmarksFragment
        )
    private var selectedIndex = 0
    private val selectedFragment get() = fragments[selectedIndex]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            albumFragment = AlbumFragment()
            bookmarksFragment = BookmarksFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, albumFragment, TAG_ALBUM_FRAGMENT)
                .add(R.id.fragment_container, bookmarksFragment, TAG_BOOKMARKS_FRAGMENT)
                .commit()
        } else {
            albumFragment =
                supportFragmentManager.findFragmentByTag(TAG_ALBUM_FRAGMENT) as AlbumFragment
            bookmarksFragment =
                supportFragmentManager.findFragmentByTag(TAG_BOOKMARKS_FRAGMENT) as BookmarksFragment

            selectedIndex = savedInstanceState.getInt(KEY_SELECTED_INDEX, 0)
        }

        selectFragment(selectedFragment)
        binding.bottomNav.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_album -> albumFragment
                R.id.nav_bookmarks -> bookmarksFragment
                else -> throw IllegalArgumentException("unexpected itemId")
            }
            selectFragment(fragment)
            true
        }
    }


    private fun selectFragment(selectedFragment: Fragment) {
        var transaction = supportFragmentManager.beginTransaction()
        fragments.forEachIndexed { index, fragment ->
            if (selectedFragment == fragment) {
                transaction = transaction.attach(fragment)
                selectedIndex = index
            } else {
                transaction = transaction.detach(fragment)
            }
        }
        transaction.commit()
        title = when (selectedFragment) {
            is AlbumFragment -> getString(R.string.title_album)
            is BookmarksFragment -> getString(R.string.title_bookmarks)
            else -> ""
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_INDEX, selectedIndex)
    }

    companion object{
        private const val TAG = "MainActivity"
    }

}

private const val KEY_SELECTED_INDEX = "KEY_SELECTED_INDEX"
private const val TAG_ALBUM_FRAGMENT = "TAG_ALBUM_FRAGMENT"
private const val TAG_BOOKMARKS_FRAGMENT = "TAG_BOOKMARKS_FRAGMENT"