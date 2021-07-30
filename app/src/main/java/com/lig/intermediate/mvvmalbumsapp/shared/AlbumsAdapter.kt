package com.lig.intermediate.mvvmalbumsapp.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.lig.intermediate.mvvmalbumsapp.data.Annonce
import com.lig.intermediate.mvvmalbumsapp.databinding.ItemAlbumBinding


class AlbumsAdapter: ListAdapter<Annonce, AlbumsViewHolder>(AnnonceComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumsViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumsViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }
}