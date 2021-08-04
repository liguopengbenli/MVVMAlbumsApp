package com.lig.mvvmalbumsapp.features.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lig.mvvmalbumsapp.R
import com.lig.mvvmalbumsapp.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val args by navArgs<DetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val annonce = args.annonce

        val binding = FragmentDetailsBinding.bind(view)
        binding.apply {
            val url = GlideUrl(
                annonce.url, LazyHeaders.Builder()
                    .addHeader("User-Agent", "your-user-agent")
                    .build()
            )
            Glide.with(this@DetailsFragment)
                .load(url)
                .error(R.drawable.image_placeholder)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        textViewAlbumId.isVisible = true
                        textViewTitle.isVisible = annonce.title != null
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        textViewAlbumId.isVisible = true
                        textViewTitle.isVisible = annonce.title != null
                        return false
                    }
                }).into(imageView)

            textViewTitle.text = annonce.title
            textViewAlbumId.text = requireContext().getString(R.string.album_id_full,  annonce.albumId)

        }

    }

}