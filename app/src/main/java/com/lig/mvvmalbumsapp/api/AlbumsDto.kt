package com.lig.mvvmalbumsapp.api

data class AlbumsDto(
    val albumId: Int,
    val id: Int,
    val title: String?,
    val url: String?,
    val thumbnailUrl: String?
)

