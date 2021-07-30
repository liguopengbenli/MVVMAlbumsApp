package com.lig.intermediate.mvvmalbumsapp.shared

import androidx.recyclerview.widget.DiffUtil
import com.lig.intermediate.mvvmalbumsapp.data.Annonce

class AnnonceComparator : DiffUtil.ItemCallback<Annonce>() {
    override fun areItemsTheSame(oldItem: Annonce, newItem: Annonce) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Annonce, newItem: Annonce): Boolean =
        oldItem == newItem

}