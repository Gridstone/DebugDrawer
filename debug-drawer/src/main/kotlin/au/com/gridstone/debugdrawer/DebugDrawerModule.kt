package au.com.gridstone.debugdrawer

import android.content.Context
import android.view.View
import android.view.ViewGroup

interface DebugDrawerModule {
  fun onAttach(context: Context) {}
  fun onDetach(context: Context) {}
  fun onCreateView(parent: ViewGroup): View
}
