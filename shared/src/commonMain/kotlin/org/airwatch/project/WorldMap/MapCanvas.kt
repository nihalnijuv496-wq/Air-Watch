package org.airwatch.project.WorldMap

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.airwatch.project.Aircraft.Coordinate

@Composable
fun DrawMapCanvas(type: String, screenWidth: Int, screenHeight: Int)
{
    var coordinateTree by remember { mutableStateOf<Chunk?>(null) }
    var projection by remember { mutableStateOf<Projection?>(null) }

    LaunchedEffect(Unit) {
        val loadedTree = withContext(Dispatchers.IO) {
            loadMapToTree()
        }
        coordinateTree = loadedTree
    }

    if(coordinateTree == null)
    {
        println("tree not loaded")
        return
    }


    projection = if(type == "Plane"){
        EquirectangularProjection(
            viewportWidth = screenWidth.toDouble(),
            viewportHeight = screenHeight.toDouble(),
        )
    }
    else{
        OrthographicProjection(
            viewportWidth = screenWidth.toDouble(),
            viewportHeight = screenHeight.toDouble(),
        )

    }

    Canvas(modifier = Modifier.fillMaxSize())
    {
        traverse(
            node = coordinateTree!!,
            projection = projection!!,
            screenWidth = screenWidth,
            screenHeight = screenHeight
        ) { node ->
            node.coordinates.forEach { coordinate ->
                val screenPos = projection!!.project(coordinate) ?: return@forEach
                drawPoint(screenPos)

            }
            println("nodeDrawn")
        }
    }
}

fun DrawScope.drawPoint(coordinate: Coordinate)
{
    drawCircle(
        color = Color.Black,
        radius = 0.5f,
        center = Offset(coordinate.longitude!!.toFloat(), coordinate.latitude!!.toFloat())
    )
}