package com.lig.intermediate.mvvmalbumsapp.data

import android.util.Log
import androidx.room.withTransaction
import com.lig.intermediate.mvvmalbumsapp.api.AlbumsApi
import com.lig.intermediate.mvvmalbumsapp.util.Resource
import com.lig.intermediate.mvvmalbumsapp.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlbumsRepository @Inject constructor(
    private val albumsApi: AlbumsApi,
    private val albumsDb: AlbumsDatabase
) {
    private val albumsDao = albumsDb.albumsDao()

    fun getAlbums(): Flow<Resource<List<Annonce>>> =
        networkBoundResource(
            query = {
                albumsDao.getAllAlbums()
            },
            fetch = {
                val response = albumsApi.getAlbums()
                response
            },
            saveFetchResult = { albums->
                val serverAlbums = albumsApi.getAlbums()
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
                albumsDb.withTransaction {
                    albumsDao.deleteAllAlbums()
                    albumsDao.insertAlbums(localAlbums)
                }
            }
        )

    companion object{
        private const val TAG = "AlbumsRepository"
    }
}