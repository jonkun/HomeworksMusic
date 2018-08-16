package lt.joku.danskehomework.util

import android.content.Context
import android.support.annotation.DrawableRes
import com.carto.styles.MarkerStyleBuilder
import com.carto.utils.BitmapUtils
import lt.joku.danskehomework.R

/**
 * @author Jonas Kundra
 * @since 1.0
 */

object MarkersStyleProvider {

    fun getPlaceMarkerStyle(context: Context) =
            getMarkerStyle(context, R.drawable.ic_location_on, 32, 0.2f)

    private fun getMarkerStyle(context: Context,
                               @DrawableRes drawableResId: Int,
                               size: Int, anchorPoint: Float) =
            getMarkerStyleBuilder(context, drawableResId, size, anchorPoint).buildStyle()

    private fun getMarkerStyleBuilder(context: Context, @DrawableRes drawableResId: Int, size: Int, anchorPoint: Float): MarkerStyleBuilder {
        val bitmap = loadDrawableAsBitmap(context, drawableResId, -1,-1)
        val markerBitmap = BitmapUtils.createBitmapFromAndroidBitmap(bitmap)
        val markerStyleBuilder = MarkerStyleBuilder()
        markerStyleBuilder.bitmap = markerBitmap
        markerStyleBuilder.size = size.toFloat()
        markerStyleBuilder.setAnchorPoint(0f, anchorPoint)
        return markerStyleBuilder
    }

}