package com.lig.intermediate.mvvmalbumsapp.shared

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.lig.intermediate.mvvmalbumsapp.R
import com.lig.intermediate.mvvmalbumsapp.data.Annonce
import com.lig.intermediate.mvvmalbumsapp.databinding.ItemAlbumBinding


class AlbumsViewHolder(
    private val binding: ItemAlbumBinding
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
                itemView.context.getString(R.string.album_id_prefix) + annonce.albumId.toString()
            imageViewBookmark.setImageResource(
                when {
                    annonce.isBookMarked -> R.drawable.ic_bookmark_selected
                    else -> R.drawable.ic_bookmark_unselected
                }
            )

        }
    }
}