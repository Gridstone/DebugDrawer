package au.com.gridstone.debugdrawer

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.SparseArray
import android.view.ContextThemeWrapper
import android.view.Gravity.END
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnApplyWindowInsetsListener
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowInsets
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.drawerlayout.widget.DrawerLayout

object DebugDrawer {

  fun with(activity: Activity): Builder = Builder(activity)

  class Builder internal constructor(private val activity: Activity) {

    private var mainContainer: ViewGroup = FrameLayout(activity)
    private var index = 0
    private val sectionTitles = SparseArray<String>(5)
    private val modules = SparseArray<DebugDrawerModule>(5)

    fun overrideMainContainer(viewGroup: ViewGroup): Builder {
      mainContainer = viewGroup
      return this
    }

    fun addSectionTitle(title: String): Builder {
      sectionTitles.put(index, title)
      index++
      return this
    }

    fun addSectionTitle(@StringRes titleRes: Int): Builder {
      return addSectionTitle(activity.getString(titleRes))
    }

    fun addModule(module: DebugDrawerModule): Builder {
      modules.put(index, module)
      index++
      return this
    }

    fun buildMainContainer(): ViewGroup {
      // Create DrawerLayout and add mainContainer as its first child.
      val drawerLayout = DrawerLayout(activity)
      drawerLayout.id = R.id.debugDrawerId
      drawerLayout.addView(mainContainer)

      val themedContext = ContextThemeWrapper(activity, R.style.Theme_DebugDrawer)
      val inflater = LayoutInflater.from(themedContext)

      // Create the ScrollView that will house the debug drawer content and add it to DrawerLayout.
      val drawerContentScrollView = DrawerScrollView(themedContext)
      val params = DrawerLayout.LayoutParams(drawerLayout.dpToPx(290), MATCH_PARENT, END)
      drawerContentScrollView.layoutParams = params
      drawerContentScrollView.id = R.id.debugDrawerId
      drawerContentScrollView.setBackgroundColor(Color.rgb(66, 66, 66))
      drawerLayout.addView(drawerContentScrollView)

      // Create and add the drawer content container to the scroll view.
      val drawerContent: ViewGroup = inflater
          .inflate(R.layout.drawer_content, drawerContentScrollView, false) as ViewGroup

      drawerContentScrollView.addView(drawerContent)

      // Add all section titles and modules to the content view.
      for (i in 0 until index) {
        val title: String? = sectionTitles[i]

        if (title != null) {
          val titleView: TextView =
              inflater.inflate(R.layout.drawer_module_title, drawerContent, false) as TextView

          titleView.text = title
          drawerContent.addView(titleView)
          continue
        }

        val module: DebugDrawerModule = modules[i] ?: throw IndexOutOfBoundsException(
            "Index has no associated title or module.")
        drawerContent.addView(module.onCreateView(drawerContent))
      }

      // If the main container is dealing with window insets then we want to know. It will
      // potentially allow us to render the debug drawer behind the status bar.
      val insetListener = InsetListener(drawerContentScrollView, drawerContent)
      mainContainer.setOnApplyWindowInsetsListener(insetListener)

      // Add the DrawerLayout to the activity, register lifecycle callbacks to inform modules of
      // attach/detach events, and return the main container the activity can use to push and pop
      // screen views.
      activity.setContentView(drawerLayout)
      activity.application.registerActivityLifecycleCallbacks(LifecycleListener(modules.toSet()))
      return mainContainer
    }
  }

  private class InsetListener(
      private val scrollView: DrawerScrollView,
      private val drawerContentContainer: View) : OnApplyWindowInsetsListener {

    override fun onApplyWindowInsets(v: View, insets: WindowInsets): WindowInsets {
      scrollView.setStatusBarHeight(insets.systemWindowInsetTop)

      val resources: Resources = v.resources
      val horizontalPadding: Int = resources.getDimensionPixelSize(R.dimen.drawerHorizontalPadding)
      val verticalPadding: Int = resources.getDimensionPixelSize(R.dimen.drawerVerticalPadding)

      drawerContentContainer.setPadding(
          horizontalPadding,
          verticalPadding + insets.systemWindowInsetTop,
          horizontalPadding,
          verticalPadding)

      return insets
    }
  }

  private class LifecycleListener(
      private val modules: Set<DebugDrawerModule>) : ActivityLifecycleCallbacks {

    override fun onActivityStarted(activity: Activity) {
      for (module in modules) {
        module.onAttach(activity)
      }
    }

    override fun onActivityStopped(activity: Activity) {
      for (module in modules) {
        module.onDetach(activity)
      }
    }

    override fun onActivityDestroyed(activity: Activity) {
      activity.application.unregisterActivityLifecycleCallbacks(this)
    }

    override fun onActivityPaused(activity: Activity?) {}
    override fun onActivityResumed(activity: Activity?) {}
    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}
    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}
  }
}
