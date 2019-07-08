package au.com.gridstone.debugdrawer.leakcanary

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import au.com.gridstone.debugdrawer.DebugDrawerModule
import au.com.gridstone.debugdrawer.leakcanary2.R
import leakcanary.LeakCanary

object LeakCanaryModule : DebugDrawerModule {

  override fun onCreateView(parent: ViewGroup): View {
    val button = Button(parent.context)
    button.setText(R.string.drawer_leakcanaryButton)
    button.setOnClickListener {
      parent.context.startActivity(LeakCanary.leakDisplayActivityIntent)
    }

    return button
  }
}
