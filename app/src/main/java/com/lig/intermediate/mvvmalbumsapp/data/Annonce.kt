package com.lig.intermediate.mvvmalbumsapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class Annonce(
    @PrimaryKey val id: Int = 0,
    val title: String?,
    val albumId: Int,
    val thumbnailUrl:String?,
    val Url:String?,
    val isBookMarked: Boolean
)