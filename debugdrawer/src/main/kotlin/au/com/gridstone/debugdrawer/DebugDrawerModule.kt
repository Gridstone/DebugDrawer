package au.com.gridstone.debugdrawer

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * A module that can be added to the [DebugDrawer].
 */
interface DebugDrawerModule {

  /**
   * Called when the hosting [Activity] has been started. Use this callback to wire up any
   * operations that may later need to be cleared in [onDetach].
   */
  fun onAttach(context: Context) {}

  /**
   * Called when the hosting [Activity] has been stopped. Use this callback to clear any operations
   * that may have been started in [onAttach].
   */
  fun onDetach(context: Context) {}

  /**
   * This method produces the view that will be displayed in the debug drawer. To achieve a cohesive
   * appearance consider making use of the styles provided for subviews, such as
   * `Widget.DebugDrawer.RowTitle`.
   */
  fun onCreateView(parent: ViewGroup): View
}
