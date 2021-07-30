package com.lig.intermediate.mvvmalbumsapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Annonce::class], version = 1)
abstract class AlbumsDatabase: RoomDatabase() {
    abstract fun albumsDao(): AlbumsDao
}