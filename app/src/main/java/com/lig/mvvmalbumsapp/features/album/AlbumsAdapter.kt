package com.lig.mvvmalbumsapp.features.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.lig.mvvmalbumsapp.data.Annonce
import com.lig.mvvmalbumsapp.databinding.ItemAlbumBinding
import com.lig.mvvmalbumsapp.shared.AlbumsViewHolder
import com.lig.mvvmalbumsapp.shared.AnnonceComparator


class AlbumsAdapter(
    private val onItemClick: (Annonce) -> Unit,
    private val onBookmarkClick: (Annonce) -> Unit
): ListAdapter<Annonce, AlbumsViewHolder>(AnnonceComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumsViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumsViewHolder(binding,
            onItemClick = { position->
                val annonce = getItem(position)
                if(annonce != null){
                    onItemClick(annonce)
                }
            },
            onBookmarkClick = { position->
                val annonce = getItem(position)
                if(annonce != null){
                    onBookmarkClick(annonce)
                }
            }
        )
    }

    override fun onBindViewHolder(holder: AlbumsViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }
}