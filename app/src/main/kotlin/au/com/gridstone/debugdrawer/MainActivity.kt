package au.com.gridstone.debugdrawer

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class MainActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val container: ViewGroup = getRootViewContainerFor(this)
    val home: View = LayoutInflater.from(this).inflate(R.layout.activity_main, container, false)
    container.addView(home)
  }
}
