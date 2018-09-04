package au.com.gridstone.debugdrawer.sampleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewAnimator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity() {

  private var getGamesJob: Job? = null
  private var cachedGamesList: List<Game> = emptyList()
  private val gamesAdapter = GamesAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Get root container to put our app's UI in. For a debug build this will have our debug drawer.
    // For a release build this will be the Activity's root container.
    val container: ViewGroup = getRootViewContainerFor(this)
    val home: View = LayoutInflater.from(this).inflate(R.layout.home, container, false)
    container.addView(home)

    val toolbar: Toolbar = findViewById(R.id.home_toolbar)
    val drawer: DrawerLayout = findViewById(R.id.home_navDrawer)
    toolbar.setNavigationOnClickListener { drawer.openDrawer(GravityCompat.START) }

    val recycler: RecyclerView = findViewById(R.id.games_recycler)
    recycler.adapter = gamesAdapter
    recycler.layoutManager = LinearLayoutManager(this)

    // Not how you'd handle this in a real app, but for a single Activity sample we can cheat
    // and pass our cached list of games between Activity instances.
    if (lastCustomNonConfigurationInstance != null) {
      @Suppress("UNCHECKED_CAST") // We retain List<Game> and nothing else.
      cachedGamesList = lastCustomNonConfigurationInstance as List<Game>
    }
  }

  override fun onStart() {
    super.onStart()

    val viewAnimator: ViewAnimator = findViewById(R.id.games_viewAnimator)

    if (cachedGamesList.isNotEmpty()) {
      viewAnimator.displayedChild = 2 // Display recycler.
      gamesAdapter.set(cachedGamesList)
    } else {
      viewAnimator.displayedChild = 0 // Display loading.

      getGamesJob = launch(UI) {
        val result: GamesResult = GamesApi.getGames()

        if (result.success) {
          cachedGamesList = result.games
          gamesAdapter.set(result.games)
          viewAnimator.displayedChild = 2 // Display recycler.
        } else {
          viewAnimator.displayedChild = 1 // Display error.
        }
      }
    }
  }

  override fun onStop() {
    super.onStop()
    getGamesJob?.cancel()
  }

  override fun onRetainCustomNonConfigurationInstance(): Any {
    return cachedGamesList
  }
}
