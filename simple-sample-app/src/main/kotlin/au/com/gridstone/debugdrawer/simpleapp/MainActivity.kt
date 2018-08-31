package au.com.gridstone.debugdrawer.simpleapp

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.TextView

class MainActivity : Activity() {

  @SuppressLint("SetTextI18n") // No need to localise this simple sample.
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val mainContainer = getRootViewContainerFor(this)

    val textView = TextView(this)
    textView.text = "Swipe from the right to show the drawer."
    textView.gravity = Gravity.CENTER
    textView.layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)

    mainContainer.addView(textView)
  }
}
