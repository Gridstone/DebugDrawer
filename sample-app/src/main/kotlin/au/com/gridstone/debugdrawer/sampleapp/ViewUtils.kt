package au.com.gridstone.debugdrawer.sampleapp

import android.graphics.Rect
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.WindowInsets
import androidx.core.view.updatePadding

fun View.updatePaddingWithInsets(left: Boolean = false,
                                 top: Boolean = false,
                                 right: Boolean = false,
                                 bottom: Boolean = false) {

  doOnApplyWindowInsets { insets, padding ->
    updatePadding(left = if (left) padding.left + insets.systemWindowInsetLeft else padding.left,
                  top = if (top) padding.top + insets.systemWindowInsetTop else padding.top,
                  right = if (right) padding.right + insets.systemWindowInsetRight else padding.right,
                  bottom = if (bottom) padding.bottom + insets.systemWindowInsetBottom else padding.bottom)
  }
}

inline fun View.doOnApplyWindowInsets(crossinline block: (insets: WindowInsets, padding: Rect) -> Unit) {
  // Create a snapshot of padding.
  val initialPadding = Rect(paddingLeft, paddingTop, paddingRight, paddingBottom)

  // Set an actual OnApplyWindowInsetsListener which proxies to the given lambda, also passing in the original padding.
  setOnApplyWindowInsetsListener { _, insets ->
    block(insets, initialPadding)
    return@setOnApplyWindowInsetsListener insets
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
