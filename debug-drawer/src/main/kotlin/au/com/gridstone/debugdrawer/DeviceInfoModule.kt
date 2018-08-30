package au.com.gridstone.debugdrawer

import android.content.Context
import android.view.View
import android.view.ViewGroup

class DeviceInfoModule(context: Context) : DebugDrawerModule {
  override val title: String = context.getString(R.string.drawer_deviceInfo_title)

  override fun onCreateView(parent: ViewGroup): View = parent.inflate(R.layout.device_info_module)
}
