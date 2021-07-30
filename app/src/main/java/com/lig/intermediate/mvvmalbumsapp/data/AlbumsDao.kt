package com.lig.intermediate.mvvmalbumsapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumsDao {

    @Query("SELECT * FROM albums")
    fun getAllAlbums(): Flow<List<Annonce>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(albums: List<Annonce>)


}