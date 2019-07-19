package au.com.gridstone.debugdrawer.leakcanary

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import au.com.gridstone.debugdrawer.DebugDrawerModule
import com.squareup.leakcanary.internal.DisplayLeakActivity

/**
 * Displays a button in the drawer to open LeakCanary's leak list activity. Note that including this
 * module in your project removes the LeakCanary launcher icon, effectively making the debug
 * drawer the main entry point into your leak list.
 */
class LeakCanaryModule : DebugDrawerModule {

  override fun onCreateView(parent: ViewGroup): View {
    val button = Button(parent.context)
    button.setText(R.string.drawer_leakcanaryButton)
    button.setOnClickListener {
      val intent = Intent(parent.context, DisplayLeakActivity::class.java)
      parent.context.startActivity(intent)
    }

    return button
  }
}
