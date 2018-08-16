package lt.joku.danskehomework.util

import com.carto.core.MapPos

/**
 * @author Jonas Kundra
 * @since 1.0
 */

object MapPosUtils {

    fun wgs84MapPos(latitude: Double, longitude: Double): MapPos {
        return MapPos(longitude, latitude)
    }
}