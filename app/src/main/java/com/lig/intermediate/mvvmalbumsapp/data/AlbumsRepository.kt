package com.lig.intermediate.mvvmalbumsapp.data

import androidx.room.withTransaction
import com.lig.intermediate.mvvmalbumsapp.api.AlbumsApi
import com.lig.intermediate.mvvmalbumsapp.util.Resource
import com.lig.intermediate.mvvmalbumsapp.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AlbumsRepository @Inject constructor(
    private val albumsApi: AlbumsApi,
    private val albumsDb: AlbumsDatabase
) {
    private val albumsDao = albumsDb.albumsDao()

    fun getAlbums(
        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchFailed: (Throwable) -> Unit
    ): Flow<Resource<List<Annonce>>> =
        networkBoundResource(
            query = {
                albumsDao.getAllAlbums()
            },
            fetch = {
                val response = albumsApi.getAlbums()
                response
            },
            saveFetchResult = { albums ->
                val serverAlbums = albumsApi.getAlbums()
                val localAlbums = serverAlbums.map { serverAnnoce ->
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
            },
            shouldFetch = {
                forceRefresh
            },
            onFetchSuccess = onFetchSuccess,
            onFetchFailed = { t ->
                if(t !is HttpException && t !is IOException){
                    throw t
                }
                onFetchFailed(t)
            }
        )

    suspend fun updateAnnonce(annonce: Annonce) {
        albumsDao.updateAnnonce(annonce)
    }

    suspend fun resetAllBookMarks() {
        albumsDao.resetAllBookmarks()
    }

    fun getAllBookMarkedAnnonces(): Flow<List<Annonce>> = albumsDao.getAllBookMarkedAnnonces()

    companion object {
        private const val TAG = "AlbumsRepository"
    }
}