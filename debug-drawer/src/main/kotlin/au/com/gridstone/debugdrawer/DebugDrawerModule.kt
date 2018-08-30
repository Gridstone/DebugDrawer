package au.com.gridstone.debugdrawer

import android.content.Context
import android.view.View
import android.view.ViewGroup

interface DebugDrawerModule {
  val title: String

  fun onAttach(context: Context) {}
  fun onDetach(context: Context) {}
  fun onCreateView(parent: ViewGroup): View
}
