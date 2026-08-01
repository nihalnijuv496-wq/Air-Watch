package org.airwatch.project.WorldMap


fun traverse(node: Chunk, projection: Projection,screenWidth: Int, screenHeight: Int, onRender: (Chunk) -> Unit) {

    val LOD_PIXEL_THRESHOLD = 5.0
    if (!projection.isChunkVisible(
            corners = node.chunkCorners,
            screenWidth = screenWidth,
            screenHeight = screenHeight
        )) {
        return
    }

    val screenSpan = projection.chunkScreenSpan(node.chunkCorners)
    if (screenSpan < LOD_PIXEL_THRESHOLD || node.children == null) {
        onRender(node)
        return
    }
    node.children?.forEach { traverse(
        node = it,
        projection = projection,
        screenWidth = screenWidth,
        screenHeight = screenHeight,
        onRender =  onRender
        ) }
}
