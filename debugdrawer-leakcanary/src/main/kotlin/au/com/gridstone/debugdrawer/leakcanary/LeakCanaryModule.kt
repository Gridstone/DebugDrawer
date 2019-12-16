package au.com.gridstone.debugdrawer.leakcanary

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import au.com.gridstone.debugdrawer.DebugDrawerModule
import leakcanary.LeakCanary

object LeakCanaryModule : DebugDrawerModule {

  private const val SHARED_PREFS_NAME = "DebugDrawer_LeakCanary"
  private const val KEY_ENABLE_HEAP_DUMPS = "enableHeapDumps"

  override fun onAttach(context: Context) {
    val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE)
    val enableHeapDumps = sharedPrefs.getBoolean(KEY_ENABLE_HEAP_DUMPS, true)
    LeakCanary.config = LeakCanary.config.copy(dumpHeap = enableHeapDumps)
  }

  override fun onCreateView(parent: ViewGroup): View {
    val inflater = LayoutInflater.from(parent.context)
    val view: View = inflater.inflate(R.layout.drawer_leakcanary, parent, false)

    val sharedPrefs = parent.context.getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE)
    val enableHeapDumps = sharedPrefs.getBoolean(KEY_ENABLE_HEAP_DUMPS, true)

    val toggle: Switch = view.findViewById(R.id.drawer_leakcanaryToggle)
    toggle.isChecked = enableHeapDumps
    toggle.setOnCheckedChangeListener { _, checked ->
      LeakCanary.config = LeakCanary.config.copy(dumpHeap = checked)
      sharedPrefs.edit().putBoolean(KEY_ENABLE_HEAP_DUMPS, checked).apply()
    }

    val button: Button = view.findViewById(R.id.drawer_leakcanaryViewButton)
    button.setOnClickListener {
      parent.context.startActivity(LeakCanary.newLeakDisplayActivityIntent())
    }

    return view
  }
}
