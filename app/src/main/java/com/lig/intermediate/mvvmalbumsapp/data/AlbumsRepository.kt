package com.lig.intermediate.mvvmalbumsapp.data

import android.util.Log
import com.lig.intermediate.mvvmalbumsapp.api.AlbumsApi
import javax.inject.Inject

class AlbumsRepository @Inject constructor(
    private val albumsApi: AlbumsApi,
    private val albumsDb: AlbumsDatabase
) {
    private val albumsDao = albumsDb.albumsDao()

    suspend fun getAlbums(): List<Annonce> {
        val response = albumsApi.getAlbums()
        val serverAlbums = response
        val localAlbums = serverAlbums.map { serverAnnoce->
            Annonce(
                id = serverAnnoce.id,
                title = serverAnnoce.title,
                albumId = serverAnnoce.albumId,
                thumbnailUrl = serverAnnoce.thumbnailUrl,
                url = serverAnnoce.url,
                isBookMarked = false
            )
        }
        return localAlbums
    }

    companion object{
        private const val TAG = "AlbumsRepository"
    }
}