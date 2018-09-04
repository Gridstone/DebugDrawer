package au.com.gridstone.debugdrawer.sampleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity() {

  private var getGamesJob: Job? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val container: ViewGroup = getRootViewContainerFor(this)
    val home: View = LayoutInflater.from(this).inflate(R.layout.home, container, false)
    container.addView(home)

    val toolbar: Toolbar = findViewById(R.id.home_toolbar)
    val drawer: DrawerLayout = findViewById(R.id.home_navDrawer)
    toolbar.setNavigationOnClickListener { drawer.openDrawer(GravityCompat.START) }

    val adapter = GamesAdapter()
    val recycler: RecyclerView = findViewById(R.id.games_recycler)
    recycler.adapter = adapter
    recycler.layoutManager = LinearLayoutManager(this)
    val loadingView: View = findViewById(R.id.games_loading)
    val errorView: View = findViewById(R.id.games_error)

    getGamesJob = launch(UI) {
      val result: GamesResult = GamesApi.getGames()
      loadingView.visibility = GONE

      if (result.success) {
        errorView.visibility = GONE
        recycler.visibility = VISIBLE
        adapter.set(result.games)
      } else {
        errorView.visibility = VISIBLE
        recycler.visibility = GONE
        val errorImageView: ImageView = findViewById(R.id.games_error_image)
        Picasso.get().load(R.drawable.gfx_dead_link_small).into(errorImageView)
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    getGamesJob?.cancel()
  }
}
