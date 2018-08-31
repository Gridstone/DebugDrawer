package au.com.gridstone.debugdrawer

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.DisplayMetrics.DENSITY_HIGH
import android.util.DisplayMetrics.DENSITY_LOW
import android.util.DisplayMetrics.DENSITY_MEDIUM
import android.util.DisplayMetrics.DENSITY_TV
import android.util.DisplayMetrics.DENSITY_XHIGH
import android.util.DisplayMetrics.DENSITY_XXHIGH
import android.util.DisplayMetrics.DENSITY_XXXHIGH
import android.util.Log
import android.view.View
import android.view.ViewGroup

class DeviceInfoModule(context: Context) : DebugDrawerModule {
  override val title: String = context.getString(R.string.drawer_deviceInfo_title)

  override fun onCreateView(parent: ViewGroup): View {

    val view: View = parent.inflate(R.layout.device_info_module)
    val resources: Resources = parent.context.resources
    val displayMetrics: DisplayMetrics = resources.displayMetrics

    val density: String = when (displayMetrics.densityDpi) {
      DENSITY_LOW -> "ldpi"
      DENSITY_MEDIUM -> "mdpi"
      DENSITY_HIGH -> "hdpi"
      DENSITY_TV -> "tvdpi"
      DENSITY_XHIGH -> "xhdpi"
      DENSITY_XXHIGH -> "xxhdpi"
      DENSITY_XXXHIGH -> "xxxhdpi"
      else -> "unknown"
    }

    return view
  }
}
