package au.com.gridstone.debugdrawer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.widget.ScrollView

/**
 * A scroll view that will draw a status bar scrim when given a height via [setStatusBarHeight].
 */
internal class DrawerScrollView(context: Context) : ScrollView(context) {
  private val paint: Paint = Paint()
  private val statusBarColour: Int = Color.argb(127, 0, 0, 0)
  private var statusBarHeight = 0

  fun setStatusBarHeight(height: Int) {
    statusBarHeight = height
    setWillNotDraw(height == 0)
  }

  override fun draw(canvas: Canvas) {
    super.draw(canvas)

    if (statusBarHeight > 0) {
      val top: Float = scrollY.toFloat()
      val bottom: Float = top + statusBarHeight

      paint.color = statusBarColour
      canvas.drawRect(0f, top, width.toFloat(), bottom, paint)
    }
  }
}
