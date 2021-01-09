package au.com.gridstone.debugdrawer.sampleapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import au.com.gridstone.debugdrawer.sampleapp.GamesAdapter.ViewHolder
import coil.load

class GamesAdapter : RecyclerView.Adapter<ViewHolder>() {
  private var games: List<Game> = emptyList()

  fun set(games: List<Game>) {
    this.games = games
    notifyDataSetChanged()
  }

  override fun getItemCount(): Int = games.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val view = inflater.inflate(R.layout.list_item_game, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bindTo(games[position])
  }

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val imageView: ImageView = view.findViewById(R.id.game_image)
    private val nameView: TextView = view.findViewById(R.id.game_name)

    fun bindTo(game: Game) {
      nameView.text = game.name

      imageView.load(game.image.small_url) {
        placeholder(R.drawable.gfx_controller)
        error(R.drawable.gfx_dead_link_small)
        crossfade(true)
      }
    }
  }
}
