package com.lig.mvvmalbumsapp.data

import androidx.room.withTransaction
import com.lig.mvvmalbumsapp.api.AlbumsApi
import com.lig.mvvmalbumsapp.util.Resource
import com.lig.mvvmalbumsapp.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AlbumsRepository @Inject constructor(
    private val albumsApi: AlbumsApi,
    private val albumsDb: AlbumsDatabase
) {
    private val albumsDao = albumsDb.albumsDao()

    fun getAlbums(
        albumId: Int,
        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchFailed: (Throwable) -> Unit
    ): Flow<Resource<List<Annonce>>> =
        networkBoundResource(
            query = {
                albumsDao.getAlbumsById(albumId)
            },
            fetch = {
                val response = albumsApi.getAlbums()
                response
            },
            saveFetchResult = { serverAlbums ->
                val bookmarkedArticles = albumsDao.getAllBookMarkedAnnonces().first()

                val localAlbums = serverAlbums.map { serverAnnoce ->
                    val isBookmarked = bookmarkedArticles.any { bookmarkedAnnonce ->
                        bookmarkedAnnonce.id == serverAnnoce.id
                    }
                    Annonce(
                        id = serverAnnoce.id,
                        title = serverAnnoce.title,
                        albumId = serverAnnoce.albumId,
                        thumbnailUrl = serverAnnoce.thumbnailUrl,
                        url = serverAnnoce.url,
                        isBookMarked = isBookmarked
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
                if (t !is HttpException && t !is IOException) {
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