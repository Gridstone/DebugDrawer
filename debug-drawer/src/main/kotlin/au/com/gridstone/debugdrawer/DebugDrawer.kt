package au.com.gridstone.debugdrawer

import android.app.Activity
import android.graphics.Color
import android.view.Gravity.END
import android.view.View
import android.view.View.OnApplyWindowInsetsListener
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowInsets
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout

object DebugDrawer {
  fun with(activity: Activity): Builder = Builder(activity)

  class Builder internal constructor(private val activity: Activity) {

    private var mainContainer: ViewGroup = FrameLayout(activity)

    fun overrideMainContainer(viewGroup: ViewGroup) {
      mainContainer = viewGroup
    }

    fun add(module: DrawerModule) {
      TODO("Add module.")
    }

    fun finishAndGetMainContainer(): ViewGroup {
      // Create DrawerLayout and add mainContainer as its first child.
      val drawerLayout = DrawerLayout(activity)
      drawerLayout.addView(mainContainer)

      // Create the ScrollView that will house the debug drawer content and add it to DrawerLayout.
      val drawerContentScrollView = DrawerScrollView(activity)
      val params = DrawerLayout.LayoutParams(drawerLayout.dpToPx(290), MATCH_PARENT, END)
      drawerContentScrollView.layoutParams = params
      drawerContentScrollView.setBackgroundColor(Color.rgb(66, 66, 66))
      drawerLayout.addView(drawerContentScrollView)

      // Create and add the drawer content container to the scroll view.
      val drawerContent = LinearLayout(activity)
      drawerContent.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
      drawerContent.orientation = VERTICAL
      drawerContent.addView(TextView(activity).apply { setText("Hello") })
      drawerContentScrollView.addView(drawerContent)

      // If the main container is dealing with window insets then we want to know. It will
      // potentially allow us to render the debug drawer behind the status bar.
      val insetListener = InsetListener(drawerContentScrollView, drawerContent)
      mainContainer.setOnApplyWindowInsetsListener(insetListener)

      // Set the DrawerLayout as the root view for the Activity and return the container the
      // Activity can use to push and pop views.
      activity.setContentView(drawerLayout)
      return mainContainer
    }
  }

  private class InsetListener(
      private val scrollView: DrawerScrollView,
      private val drawerContentContainer: View) : OnApplyWindowInsetsListener {

    override fun onApplyWindowInsets(v: View, insets: WindowInsets): WindowInsets {
      scrollView.setStatusBarHeight(insets.systemWindowInsetTop)
      drawerContentContainer.setPadding(0, insets.systemWindowInsetTop, 0, 0)
      return insets
    }
  }
}
