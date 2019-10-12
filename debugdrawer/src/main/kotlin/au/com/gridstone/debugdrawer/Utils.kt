package au.com.gridstone.debugdrawer

import android.content.res.Resources
import android.util.LayoutDirection
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.ViewGroup
import android.view.WindowInsets

internal fun Resources.dpToPx(dp: Int) = (dp * displayMetrics.density).toInt()
internal fun View.dpToPx(dp: Int): Int = resources.dpToPx(dp)

inline fun <reified T : View> ViewGroup.inflate(layout: Int, attach: Boolean = false): T {
  val view: View = LayoutInflater.from(context).inflate(layout, this, attach)
  require(view is T) { "Inflated view does not match the target type." }
  return view
}

internal fun View.isRtl(): Boolean = layoutDirection == LayoutDirection.RTL

internal inline fun View.doOnLayout(crossinline action: (view: View) -> Unit) {
  if (isLaidOut && !isLayoutRequested) {
    action(this)
  } else {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
      override fun onLayoutChange(
          view: View,
          left: Int,
          top: Int,
          right: Int,
          bottom: Int,
          oldLeft: Int,
          oldTop: Int,
          oldRight: Int,
          oldBottom: Int
      ) {
        view.removeOnLayoutChangeListener(this)
        action(view)
      }
    })
  }
}

internal inline fun View.doOnApplyWindowInsets(crossinline block: (v: View, insets: WindowInsets) -> Unit) {
  setOnApplyWindowInsetsListener { v, insets ->
    block(v, insets)
    insets
  }

  if (isAttachedToWindow) {
    // We're already attached, just request as normal.
    requestApplyInsets()
  } else {
    // We're not attached to the hierarchy. Add a listener to request when we are.
    addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
      override fun onViewAttachedToWindow(v: View) {
        v.removeOnAttachStateChangeListener(this)
        v.requestApplyInsets()
      }

      override fun onViewDetachedFromWindow(v: View) = Unit
    })
  }
}

internal fun <T> SparseArray<T>.toSet(): Set<T> {
  val set = LinkedHashSet<T>(this.size())

  for (i in 0 until this.size()) {
    set.add(this.valueAt(i))
  }

  return set
}
