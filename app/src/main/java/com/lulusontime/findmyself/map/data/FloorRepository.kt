package com.lulusontime.findmyself.map.data

import com.lulusontime.findmyself.map.data.retrofit.ApiService
import retrofit2.HttpException
import retrofit2.Response

class FloorRepository private constructor(private val apiService: ApiService) {
    suspend fun getFloor(floorId: Int): GeojsonResponse {
        return apiService.getFloorPlan(floorId)
    }

    suspend fun getFirstFloor(): GeojsonResponse {
        val floorInfos = apiService.getFloorIds()
        if (floorInfos.shortFloorInfoResponse.isEmpty()) {
            throw Exception("No floors in system")
        }
        return getFloor(floorInfos.shortFloorInfoResponse[0].id)
    }

    companion object {
        @Volatile
        private var INSTANCE: FloorRepository? = null

        fun getInstance(apiService: ApiService): FloorRepository {
            return INSTANCE ?: synchronized(this) {
                return INSTANCE ?: FloorRepository(apiService).also {
                    INSTANCE = it
                }
            }
        }
    }
}