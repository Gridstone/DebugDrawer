package au.com.gridstone.debugdrawer.simpleapp

import android.app.Activity
import android.view.ViewGroup
import au.com.gridstone.debugdrawer.DebugDrawer
import au.com.gridstone.debugdrawer.DeviceInfoModule

fun getRootViewContainerFor(activity: Activity): ViewGroup {
  return DebugDrawer.with(activity)
      .addSectionTitle("Device information")
      .addModule(DeviceInfoModule())
      .finishAndGetMainContainer()
}
