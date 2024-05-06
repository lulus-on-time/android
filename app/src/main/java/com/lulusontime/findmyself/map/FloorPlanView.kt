package com.lulusontime.findmyself.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.example.findmyself.ui.theme.PrimaryBlue
import com.example.findmyself.ui.theme.SecondaryWhite
import com.lulusontime.findmyself.map.data.GeojsonResponse

@Composable
fun FloorPlanView(modifier: Modifier = Modifier, geoJsonResponse: GeojsonResponse, myLoc:
List<Double>, scale: Float) {
    val density = LocalDensity.current.density
    val textMeasurer = rememberTextMeasurer()

    val maxX = geoJsonResponse.floor.maxX
    val maxY = geoJsonResponse.floor.maxY

    Canvas(
        modifier = modifier
            .width((maxX / density * scale).dp + 100.dp)
            .height((maxY / density * scale).dp + 100.dp)
            .padding(start = 50.dp, top = 50.dp)
    ) {
        geoJsonResponse.geojson.features.forEach { room ->
            if (room.properties.category != "corridor") {
                val path = drawRoom(room.geometry.coordinates[0], maxY, scale)
                drawPath(path, Color.Gray.copy(alpha = 0.3f))
                drawPath(path, SecondaryWhite, style = Stroke(width = 10f))

                val roomName = room.properties.name.split(" ").joinToString("\n")
                val poi = room.properties.poi
                drawText(
                    textMeasurer = textMeasurer,
                    text = roomName,
                    topLeft = Offset(
                        x = poi[1].toFloat() * scale - textMeasurer.measure(roomName).size.width / 2,
                        y = (maxY - poi[0]).toFloat() * scale
                    )
                )
            }
        }

//        Draw My Location
        drawCircle(
            color = PrimaryBlue,
            radius = 20f,
            center = Offset(myLoc[1].toFloat() * scale, (maxY - myLoc[0]).toFloat() * scale)
        )
        drawCircle(
            color = Color.White,
            radius = 25f,
            center = Offset(myLoc[1].toFloat() * scale, (maxY - myLoc[0]).toFloat() * scale),
            style = Stroke(width = 10f)
        )
    }
}

private fun drawRoom(coordinates: List<List<Double>>, sizeHeight: Double, scale: Float): Path {
    val path = Path().apply {
        coordinates.forEachIndexed { index, coordinates ->
            if (index == 0) {
                moveTo(
                    coordinates[0].toFloat() * scale,
                    (sizeHeight - coordinates[1]).toFloat() * scale
                )
            } else {
                lineTo(
                    coordinates[0].toFloat() * scale,
                    (sizeHeight - coordinates[1]).toFloat() * scale
                )
            }
        }
    }
    return path
}