package au.com.gridstone.debugdrawer.sampleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import au.com.gridstone.debugdrawer.getRootViewContainerFor

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val container: ViewGroup = getRootViewContainerFor(this)
    val home: View = LayoutInflater.from(this).inflate(R.layout.home, container, false)
    container.addView(home)

    val toolbar: Toolbar = findViewById(R.id.home_toolbar)
    val drawer: DrawerLayout = findViewById(R.id.home_navDrawer)
    toolbar.setNavigationOnClickListener { drawer.openDrawer(GravityCompat.START) }
  }
}
