package com.lig.intermediate.mvvmalbumsapp.shared

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lig.intermediate.mvvmalbumsapp.R
import com.lig.intermediate.mvvmalbumsapp.data.Annonce
import com.lig.intermediate.mvvmalbumsapp.databinding.ItemAlbumBinding

class AlbumsViewHolder(
    private val binding: ItemAlbumBinding
): RecyclerView.ViewHolder(binding.root)
{
    fun bind(annonce: Annonce) {
        binding.apply {
            Glide.with(itemView)
                .load(annonce.thumbnailUrl)
                .error(R.drawable.image_placeholder)
                .into(imageView)

            textViewTitle.text = annonce.title ?: ""
            textViewAlbumId.text = itemView.context.getString(R.string.album_id_prefix) + annonce.albumId.toString()
            imageViewBookmark.setImageResource(
                when{
                    annonce.isBookMarked -> R.drawable.ic_bookmark_selected
                    else-> R.drawable.ic_bookmark_unselected
                }
            )

        }
    }
}