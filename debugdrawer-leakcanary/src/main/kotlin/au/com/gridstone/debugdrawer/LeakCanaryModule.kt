package au.com.gridstone.debugdrawer

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.squareup.leakcanary.internal.DisplayLeakActivity

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
