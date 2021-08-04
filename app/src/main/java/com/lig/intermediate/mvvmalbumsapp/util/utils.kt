package com.lig.intermediate.mvvmalbumsapp.util

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.showSnackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_LONG,
    view: View = requireView()
) {
    Snackbar.make(view, message, duration).show()
}


inline fun SearchView.onQueryTextSubmit(crossinline listener: (Int) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            val parsedInt = query?.toIntOrNull()

            if (parsedInt != null) {
                listener(parsedInt)
            }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return true
        }

    })
}

val <T> T.exhaustive: T
         get() = this
