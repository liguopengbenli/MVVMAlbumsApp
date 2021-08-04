package com.lig.mvvmalbumsapp.shared

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.lig.mvvmalbumsapp.R
import com.lig.mvvmalbumsapp.data.Annonce
import com.lig.mvvmalbumsapp.databinding.ItemAlbumBinding


class AlbumsViewHolder(
    private val binding: ItemAlbumBinding,
    private val onItemClick: (Int) -> Unit,
    private val onBookmarkClick: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(annonce: Annonce) {

        binding.apply {
            // add this header to adapte https://via.placeholder.com
            val url = GlideUrl(
                annonce.thumbnailUrl, LazyHeaders.Builder()
                    .addHeader("User-Agent", "your-user-agent")
                    .build()
            )
            Glide.with(itemView)
                .load(url)
                .error(R.drawable.image_placeholder)
                .into(imageView)

            textViewTitle.text = annonce.title ?: ""
            textViewAlbumId.text =
                itemView.context.getString(R.string.album_id_full, annonce.albumId)
            imageViewBookmark.setImageResource(
                when {
                    annonce.isBookMarked -> R.drawable.ic_bookmark_selected
                    else -> R.drawable.ic_bookmark_unselected
                }
            )

        }
    }

    init {
        binding.apply {
            root.setOnClickListener {
                val position = bindingAdapterPosition
                if(position != RecyclerView.NO_POSITION){ // important, because the item may be deleted
                    onItemClick(position)
                }
            }

            imageViewBookmark.setOnClickListener {
                val position = bindingAdapterPosition
                if(position != RecyclerView.NO_POSITION){ // important, because the item may be deleted
                    onBookmarkClick(position)
                }
            }
        }
    }
}