package com.lig.intermediate.mvvmalbumsapp.di

import android.app.Application
import androidx.room.Room
import com.lig.intermediate.mvvmalbumsapp.api.AlbumsApi
import com.lig.intermediate.mvvmalbumsapp.data.AlbumsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providerRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(AlbumsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideAlbumsApi(retrofit: Retrofit): AlbumsApi =
        retrofit.create(AlbumsApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(application: Application): AlbumsDatabase =
        Room.databaseBuilder(application, AlbumsDatabase::class.java, "albums database")
            .fallbackToDestructiveMigration() //demo propose TODO remove
            .build()
}