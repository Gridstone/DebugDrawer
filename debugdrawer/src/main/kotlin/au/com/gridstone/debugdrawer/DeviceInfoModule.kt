package au.com.gridstone.debugdrawer

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Build.VERSION
import android.util.DisplayMetrics
import android.util.DisplayMetrics.DENSITY_HIGH
import android.util.DisplayMetrics.DENSITY_LOW
import android.util.DisplayMetrics.DENSITY_MEDIUM
import android.util.DisplayMetrics.DENSITY_TV
import android.util.DisplayMetrics.DENSITY_XHIGH
import android.util.DisplayMetrics.DENSITY_XXHIGH
import android.util.DisplayMetrics.DENSITY_XXXHIGH

/**
 * A simple module that displays
 *  - Manufacturer
 *  - Model
 *  - Screen resolution
 *  - Screen density
 *  - Current API version
 */
class DeviceInfoModule : KeyValueDebugDrawerModule() {
  override fun getEntries(context: Context): Map<String, String> {
    val resources: Resources = context.resources
    val displayMetrics: DisplayMetrics = resources.displayMetrics

    val densityBucket: String = when (displayMetrics.densityDpi) {
      in 0..DENSITY_LOW -> "ldpi"
      in DENSITY_LOW..DENSITY_MEDIUM -> "mdpi"
      DENSITY_TV -> "tvdpi"
      in DENSITY_MEDIUM..DENSITY_HIGH -> "hdpi"
      in DENSITY_HIGH..DENSITY_XHIGH -> "xhdpi"
      in DENSITY_XHIGH..DENSITY_XXHIGH -> "xxhdpi"
      in DENSITY_XXHIGH..DENSITY_XXXHIGH -> "xxxhdpi"
      else -> "unknown"
    }

    val make = resources.getString(R.string.drawer_deviceInfo_make)
    val model = resources.getString(R.string.drawer_deviceInfo_model)
    val resolution = resources.getString(R.string.drawer_deviceInfo_resolution)
    val density = resources.getString(R.string.drawer_deviceInfo_density)
    val api = resources.getString(R.string.drawer_deviceInfo_api)

    return mapOf(
        make to Build.MANUFACTURER.take(20),
        model to Build.MODEL.take(20),
        resolution to "${displayMetrics.widthPixels} x ${displayMetrics.heightPixels}",
        density to "${displayMetrics.densityDpi}dpi $densityBucket",
        api to VERSION.SDK_INT.toString()
    )
  }
}
