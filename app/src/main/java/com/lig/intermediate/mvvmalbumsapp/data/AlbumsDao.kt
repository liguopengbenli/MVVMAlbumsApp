package com.lig.intermediate.mvvmalbumsapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumsDao {

    @Query("SELECT * FROM albums")
    fun getAllAlbums(): Flow<List<Annonce>>

    @Query("SELECT * FROM albums Where :albumId = albumId ")
    fun getAlbumsById(albumId: Int): Flow<List<Annonce>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(albums: List<Annonce>)

    @Query("DELETE FROM albums")
    suspend fun deleteAllAlbums()

    @Update
    suspend fun updateAnnonce(annonce: Annonce)

    @Query("SELECT * FROM albums WHERE isBookMarked = 1 ")
    fun getAllBookMarkedAnnonces(): Flow<List<Annonce>>

    @Query("UPDATE albums SET isBookMarked = 0")
    suspend fun resetAllBookmarks()

}