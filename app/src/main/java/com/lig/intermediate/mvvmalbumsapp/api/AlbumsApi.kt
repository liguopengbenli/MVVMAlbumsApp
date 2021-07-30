package com.lig.intermediate.mvvmalbumsapp.api

import retrofit2.http.GET

interface AlbumsApi {
    companion object {
        const val BASE_URL = "https://static.leboncoin.fr/"
    }

    @GET("img/shared/technical-test.json")
    suspend fun getAlbums(): List<AlbumsDto>
}