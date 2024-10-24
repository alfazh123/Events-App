package com.example.submisiakhir.data.remote.retrofit

import com.example.submisiakhir.data.remote.response.DetailEventResponse
import retrofit2.Call
import com.example.submisiakhir.data.remote.response.EventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events?active=1")
    suspend fun getEventActive(): EventResponse

    @GET("events?active=0")
    suspend fun getEventFinished(): EventResponse

    @GET("events/{id}")
    suspend fun getEventDetail(
        @Path("id") id: Int
    ): DetailEventResponse

    @GET("events")
    suspend fun getEventSearch(
        @Query("active") active: Int = -1,
        @Query("q") keyword: String
    ): EventResponse

}