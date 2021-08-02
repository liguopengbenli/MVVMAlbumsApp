package com.lig.intermediate.mvvmalbumsapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "albums")
@Parcelize
data class Annonce(
    @PrimaryKey val id: Int = 0,
    val title: String?,
    val albumId: Int,
    val thumbnailUrl: String?,
    val url: String?,
    val isBookMarked: Boolean
): Parcelable