package au.com.gridstone.debugdrawer.sampleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ViewAnimator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.com.gridstone.debugdrawer.sampleapp.AppConfiguration.getRootViewContainerFor
import au.com.gridstone.debugdrawer.sampleapp.GamesViewModel.State
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Render under the status and navigation bars.
    window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE

    // Get root container to put our app's UI in. For a debug build this will have our debug drawer.
    // For a release build this will be the Activity's root container.
    val container: ViewGroup = getRootViewContainerFor(this)
    val home: View = LayoutInflater.from(this).inflate(R.layout.home, container, false)
    container.addView(home)

    // Wire up navigation drawer to open on toolbar button clicks.
    val toolbar: Toolbar = findViewById(R.id.home_toolbar)
    val drawer: DrawerLayout = findViewById(R.id.home_navDrawer)
    toolbar.setNavigationOnClickListener { drawer.openDrawer(GravityCompat.START) }

    val recycler: RecyclerView = findViewById(R.id.games_recycler)
    val adapter = GamesAdapter()
    recycler.adapter = adapter
    recycler.layoutManager = LinearLayoutManager(this)

    // Make children respond to window insets.
    val appBar: View = findViewById(R.id.home_appBar)
    val loadingView: View = findViewById(R.id.games_loading)
    val errorView: View = findViewById(R.id.games_loading)
    appBar.updatePaddingWithInsets(left = true, top = true, right = true)
    loadingView.updatePaddingWithInsets(left = true, right = true, bottom = true)
    errorView.updatePaddingWithInsets(left = true, right = true, bottom = true)
    recycler.updatePaddingWithInsets(left = true, right = true, bottom = true)

    val viewAnimator: ViewAnimator = findViewById(R.id.games_viewAnimator)
    val viewModel: GamesViewModel = ViewModelProviders.of(this).get()

    // Observe ViewModel state and change UI accordingly.
    viewModel.states.observe(this) { state ->
      when (state) {
        State.Idle, State.Loading -> viewAnimator.displayedChild = 0

        State.Error -> {
          viewAnimator.displayedChild = 1
          val errorImageView: ImageView = findViewById(R.id.games_error_image)
          Picasso.get().load(R.drawable.gfx_dead_link_small).into(errorImageView)
        }

        is State.Success -> {
          adapter.set(state.games)
          viewAnimator.displayedChild = 2
        }
      }
    }

    // Put refresh button in toolbar menu and have it refresh the games list.
    toolbar.inflateMenu(R.menu.home)
    toolbar.setOnMenuItemClickListener {
      viewModel.refresh()
      return@setOnMenuItemClickListener true
    }
  }

  override fun onStart() {
    super.onStart()

    val viewModel: GamesViewModel = ViewModelProviders.of(this).get()
    viewModel.refreshIfNecessary()
  }
}
