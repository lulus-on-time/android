package com.lulusontime.findmyself.wifiscan.data

import com.google.gson.annotations.SerializedName

data class WssResponseBody(

	@field:SerializedName("data")
	val data: Data = Data()
)

data class Data(

	@field:SerializedName("location")
	val location: List<Double> = listOf(0.0, 0.0),

	@field:SerializedName("poi")
	val poi: String = "",

	@field:SerializedName("floorId")
	val floorId: Int = -1,
)
