package lt.joku.danskehomework.service

import com.carto.core.MapBounds
import com.carto.core.MapPos
import com.carto.core.ScreenBounds
import com.carto.core.ScreenPos
import com.carto.datasources.LocalVectorDataSource
import com.carto.layers.CartoBaseMapStyle
import com.carto.layers.CartoOnlineVectorTileLayer
import com.carto.layers.VectorLayer
import com.carto.ui.MapView
import com.carto.vectorelements.Marker
import lt.joku.danskehomework.util.MapPosUtils
import lt.joku.danskehomework.util.MarkersStyleProvider

class MapServiceImpl(val mapView: MapView): MapService {

    override lateinit var markersHolder: LocalVectorDataSource

    init {
        /* Create and add map layers*/
        mapView.layers.add(createBaseLayer())
        mapView.layers.add(createMarkersLayer())
    }

    override fun createMarker(lat: Double, lon: Double): Marker {
        val mapPos = mapView.options.baseProjection.fromWgs84(MapPosUtils.wgs84MapPos(lat, lon))
        val marker = Marker(mapPos, MarkersStyleProvider.getPlaceMarkerStyle(mapView.context))
        return marker
    }

    override fun zoomInByBounds(localVectorDataSource: LocalVectorDataSource, padding: Int) {
        val boundsInternal = getPolygonsBounds(localVectorDataSource)
        zoomInByBounds(boundsInternal, padding, 0f)
    }

    private fun createBaseLayer() =
            CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER)

    private fun createMarkersLayer(): VectorLayer {
        markersHolder = LocalVectorDataSource(mapView.options.baseProjection)
        return VectorLayer(markersHolder)
    }

    private fun zoomInByBounds(boundsInternal: MapBounds, padding: Int, animationDuration: Float) {
        var paddingLocal = padding
        /* Check map bounds */
        if (java.lang.Double.isNaN(boundsInternal.min.x) ||
                java.lang.Double.isNaN(boundsInternal.min.y) ||
                java.lang.Double.isNaN(boundsInternal.max.x) ||
                java.lang.Double.isNaN(boundsInternal.max.y)) {
            return
        }

        /*  Ensure that padding leaves some space for bounds */
        paddingLocal = Math.min(Math.min(paddingLocal, mapView.width / 3), mapView.height / 3)

        /* Zoom in by bounds */
        mapView.moveToFitBounds(boundsInternal, ScreenBounds(ScreenPos(paddingLocal.toFloat(), paddingLocal.toFloat()),
                ScreenPos((mapView.width - paddingLocal).toFloat(), (mapView.height - paddingLocal).toFloat())), false, animationDuration)
    }

    private fun getPolygonsBounds(polygons: LocalVectorDataSource): MapBounds {
        /* Set default bounds */
        var bounds = MapBounds(MapPos(java.lang.Double.NaN, java.lang.Double.NaN), MapPos(java.lang.Double.NaN, java.lang.Double.NaN))
        if (polygons.all.isEmpty) {
            return bounds
        }
        /* Assign fist bounds */
        var polBounds = polygons.all.get(0).bounds
        var minX = polBounds.min.x
        var minY = polBounds.min.y
        var maxX = polBounds.max.x
        var maxY = polBounds.max.y

        for (i in 0 until polygons.all.size()) {
            polBounds = polygons.all.get(i.toInt()).bounds
            /* Check minimum point */
            if (polBounds.min.x < minX) minX = polBounds.min.x
            if (polBounds.min.y < minY) minY = polBounds.min.y
            /* Check maximum point */
            if (polBounds.min.x > maxX) maxX = polBounds.min.x
            if (polBounds.min.y > maxY) maxY = polBounds.min.y
        }
        bounds = MapBounds(MapPos(minX, minY), MapPos(maxX, maxY))

        return bounds
    }
}