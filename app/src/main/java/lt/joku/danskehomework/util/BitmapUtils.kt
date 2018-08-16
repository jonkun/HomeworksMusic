package lt.joku.danskehomework.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat

/**
 * @author Jonas Kundra
 * @since 1.0
 */

fun loadDrawableAsBitmap(context: Context, drawableId: Int, width: Int, height: Int): Bitmap {
    var drawable = ContextCompat.getDrawable(context, drawableId)
    return if (drawable is BitmapDrawable) {
        drawable.bitmap
    } else {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = drawable?.let { DrawableCompat.wrap(it).mutate() }
        }

        val w = drawable?.intrinsicWidth as Int
        val h = drawable.intrinsicHeight

        val bitmap = Bitmap.createBitmap(
                if (width == -1) w else width,
                if (height == -1) h else height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}