package au.com.gridstone.debugdrawer

import android.app.Activity
import android.graphics.Color
import android.view.Gravity.END
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.ScrollView
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
      val drawerLayout = DrawerLayout(activity)
      drawerLayout.addView(mainContainer)

      val drawerContentContainer = ScrollView(activity)
      val params = DrawerLayout.LayoutParams(drawerLayout.dpToPx(290), MATCH_PARENT, END)
      drawerContentContainer.layoutParams = params
      drawerContentContainer.setBackgroundColor(Color.rgb(66, 66, 66))
      drawerLayout.addView(drawerContentContainer)


      activity.setContentView(drawerLayout)
      return mainContainer
    }
  }
}
