package com.lig.intermediate.mvvmalbumsapp.data

import com.lig.intermediate.mvvmalbumsapp.api.AlbumsApi
import javax.inject.Inject

class AlbumsRepository @Inject constructor(
    private val albumsApi: AlbumsApi,
    private val albumsDb: AlbumsDatabase
) {
    private val albumsDao = albumsDb.albumsDao()
}