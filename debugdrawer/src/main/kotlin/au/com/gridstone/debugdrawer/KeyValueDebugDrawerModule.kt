package au.com.gridstone.debugdrawer

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.GridLayout
import android.widget.TextView

/**
 * This simplifies the process of creating a module that only displays key-value pairs. Override
 * [getEntries] and this class will take care of the rest.
 */
abstract class KeyValueDebugDrawerModule : DebugDrawerModule {
  final override fun onCreateView(parent: ViewGroup): View {
    val layout = GridLayout(parent.context)
    layout.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    layout.columnCount = 2
    layout.setPaddingRelative(parent.dpToPx(8), 0, 0, 0)

    val entries: Map<String, String> = getEntries(parent.context)

    for ((key, value) in entries) {
      val keyView: TextView = layout.inflate(R.layout.drawer_key) as TextView
      val valueView: TextView = layout.inflate(R.layout.drawer_value) as TextView
      keyView.text = key
      valueView.text = value
      layout.addView(keyView)
      layout.addView(valueView)
    }

    return layout
  }

  /**
   * Return a map of keys to values that will be displayed in the drawer.
   */
  abstract fun getEntries(context: Context): Map<String, String>
}
