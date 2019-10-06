package au.com.gridstone.debugdrawer

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.SparseArray
import android.view.ContextThemeWrapper
import android.view.Gravity.END
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.drawerlayout.widget.DrawerLayout

/**
 * The main entry point when adding a debug drawer to your application. Typical usage looks
 * something like:
 *
 * ```
 * fun getRootViewContainerFor(activity: Activity): ViewGroup {
 *   return DebugDrawer.with(activity)
 *       .addSectionTitle("Logs")
 *       .addModule(TimberModule())
 *       .addModule(LeakCanaryModule())
 *       .addSectionTitle("Device information")
 *       .addModule(DeviceInfoModule())
 *       .buildMainContainer()
 * }
 * ```
 *
 * The [ViewGroup] produced by [DebugDrawer.Builder.buildMainContainer] will be a child of a
 * [DrawerLayout] that the activity can use to push and pop views as the user navigates through the
 * app. Swiping from the "end" side of the screen will reveal the debug drawer, displaying all
 * titles and modules that were added to the builder.
 *
 * Titles and modules are displayed in the order in which they're added to the builder. If you want
 * to add custom modules to the drawer you can provide implementations of [DebugDrawerModule].
 */
object DebugDrawer {

  /**
   * Begin building a debug drawer for the specified activity.
   */
  fun with(activity: Activity): Builder = Builder(activity)

  /**
   * Used to configure and build a debug drawer.
   */
  class Builder internal constructor(private val activity: Activity) {

    private var mainContainer: ViewGroup? = null
    private var index = 0
    private val sectionTitles = SparseArray<String>(5)
    private val modules = SparseArray<DebugDrawerModule>(5)

    /**
     * The default [ViewGroup] returned by [buildMainContainer] is a [FrameLayout], however this
     * method can be used to override the container that will be returned by that method.
     */
    fun overrideMainContainer(viewGroup: ViewGroup): Builder {
      mainContainer = viewGroup
      return this
    }

    /**
     * Add a heading that will be displayed above subsequently added modules. This is useful for
     * creating a grouping of controls in the drawer.
     */
    fun addSectionTitle(title: String): Builder {
      sectionTitles.put(index, title)
      index++
      return this
    }

    /**
     * A version of [addSectionTitle] that takes a string resource.
     */
    fun addSectionTitle(@StringRes titleRes: Int): Builder {
      return addSectionTitle(activity.getString(titleRes))
    }

    /**
     * Add a [DebugDrawerModule] to the drawer, which may be one or many views.
     */
    fun addModule(module: DebugDrawerModule): Builder {
      modules.put(index, module)
      index++
      return this
    }

    /**
     * Use all added titles and modules to produce a [DrawerLayout] that houses both the debug
     * drawer and a container container. This `DrawerLayout` is added to the specified [Activity]
     * then this method returns the container the `Activity` can use to push and pop different
     * screens.
     *
     * This means that the calling `Activity` should not use the traditional
     * [Activity.setContentView], but rather should use the [ViewGroup] returned by this method.
     */
    fun buildMainContainer(): ViewGroup {
      // Create DrawerLayout and add mainContainer as its first child.
      val drawerLayout = DrawerLayout(activity)
      drawerLayout.id = R.id.debugDrawerId
      val container: ViewGroup = mainContainer ?: FrameLayout(activity)
      drawerLayout.addView(container)

      // Create a ScrollView to house the drawer content.
      val themedContext = ContextThemeWrapper(activity, R.style.Theme_DebugDrawer)
      val scrollView = ScrollView(themedContext)
      val params = DrawerLayout.LayoutParams(drawerLayout.dpToPx(290), MATCH_PARENT, END)
      scrollView.layoutParams = params
      scrollView.id = R.id.debugDrawerScrollId
      scrollView.setBackgroundColor(Color.rgb(66, 66, 66))
      scrollView.clipToPadding = false

      // If the main container is dealing with window insets then update the ScrollView's
      // padding accordingly.
      container.doOnApplyWindowInsets { v, insets ->
        val leftPadding: Int = if (v.isRtl()) insets.systemWindowInsetLeft else -1
        val rightPadding: Int = if (v.isRtl()) -1 else insets.systemWindowInsetRight
        scrollView.setPadding(
            leftPadding,
            insets.systemWindowInsetTop,
            rightPadding,
            insets.systemWindowInsetBottom)
      }

      drawerLayout.addView(scrollView)

      // Create and add the drawer content container to the scroll view.
      val drawerContent: ViewGroup = scrollView.inflate(R.layout.drawer_content)
      scrollView.addView(drawerContent)

      // Add all section titles and modules to the content view.
      for (i in 0 until index) {
        val title: String? = sectionTitles[i]

        if (title != null) {
          val titleView: TextView = drawerContent.inflate(R.layout.drawer_module_title)
          titleView.text = title
          drawerContent.addView(titleView)
          continue
        }

        val module: DebugDrawerModule = modules[i] ?: throw IndexOutOfBoundsException(
            "Index has no associated title or module.")
        drawerContent.addView(module.onCreateView(drawerContent))
      }

      // Exclude gestures on the top 200dp on the end-side of the DrawerLayout. This allows the
      // drawer to be revealed even with gesture navigation enabled.
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        drawerLayout.doOnLayout { view ->
          val rect = Rect(0, 0, 0, view.dpToPx(200))

          if (view.isRtl()) {
            rect.left = 0
            rect.right = view.dpToPx(32)
          } else {
            rect.left = view.width - view.dpToPx(32)
            rect.right = view.width
          }

          view.systemGestureExclusionRects = listOf(rect)
        }
      }

      // Add the DrawerLayout to the activity, register lifecycle callbacks to inform modules of
      // attach/detach events, and return the main container the activity can use to push and pop
      // screen views.
      activity.setContentView(drawerLayout)
      activity.application.registerActivityLifecycleCallbacks(LifecycleListener(modules.toSet()))
      return container
    }
  }

  /**
   * Listen for `onActivityStarted()` and `onActivityStopped()` callbacks and use them to invoke
   * `onAttach()` and `onDetached()` on all modules in the drawer.
   */
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

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
  }
}
