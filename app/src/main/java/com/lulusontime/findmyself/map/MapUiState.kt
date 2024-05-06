package com.lulusontime.findmyself.map

import com.lulusontime.findmyself.map.data.GeojsonResponse

data class MapUiState (
    val geojsonResponse: GeojsonResponse = GeojsonResponse(),
    val scale: Float = 2.5f,
    val myLoc: List<Double> = listOf(
        140.0000629075365,
        602.0156211841108
    ),
    val isMapReady: Boolean = false
)