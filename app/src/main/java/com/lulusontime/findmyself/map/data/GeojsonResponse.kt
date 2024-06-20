package com.lulusontime.findmyself.map.data

import com.google.gson.annotations.SerializedName

data class GeojsonResponse(

	@field:SerializedName("geojson")
	val geojson: Geojson = Geojson(features = emptyList(), type = ""),

	@field:SerializedName("floor")
	val floor: Floor = Floor(0, 0.0, 0.0, "", -2)
)

data class Floor(

	@field:SerializedName("level")
	val level: Int,

	@field:SerializedName("maxY")
	val maxY: Double,

	@field:SerializedName("maxX")
	val maxX: Double,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)

data class Geojson(

	@field:SerializedName("features")
	val features: List<FeaturesItem>,

	@field:SerializedName("type")
	val type: String
)

data class FeaturesItem(

	@field:SerializedName("geometry")
	val geometry: Geometry,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("properties")
	val properties: Properties
)

data class Properties(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("poi")
	val poi: List<Double>,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("category")
	val category: String
)

data class Geometry(

	@field:SerializedName("coordinates")
	val coordinates: List<List<List<Double>>>,

	@field:SerializedName("type")
	val type: String
)
