package lt.joku.danskehomework.service

import com.carto.datasources.LocalVectorDataSource
import com.carto.vectorelements.Marker

interface MapService {

    val markersHolder: LocalVectorDataSource
    fun createMarker(lat: Double, lon: Double): Marker
    fun zoomInByBounds(localVectorDataSource: LocalVectorDataSource, padding: Int)
}