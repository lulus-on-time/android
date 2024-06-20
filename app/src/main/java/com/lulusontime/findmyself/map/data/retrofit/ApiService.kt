package com.lulusontime.findmyself.map.data.retrofit

import com.lulusontime.findmyself.map.data.GeojsonResponse
import com.lulusontime.findmyself.map.data.ShortFloorInfoResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("floors/{id}")
    suspend fun getFloorPlan(
        @Path("id") id: Int
    ): GeojsonResponse

    @GET("floors/short")
    suspend fun getFloorIds(): ShortFloorInfoResponse
}