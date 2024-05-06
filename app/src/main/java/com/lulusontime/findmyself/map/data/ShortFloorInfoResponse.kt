package com.lulusontime.findmyself.map.data

import com.google.gson.annotations.SerializedName

data class ShortFloorInfoResponse(

	@field:SerializedName("ShortFloorInfoResponse")
	val shortFloorInfoResponse: List<ShortFloorInfoResponseItem>
)

data class ShortFloorInfoResponseItem(

	@field:SerializedName("level")
	val level: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)
