package au.com.gridstone.debugdrawer

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

internal class LogsAdapter : BaseAdapter() {

  private var logs: MutableList<Entry> = mutableListOf()

  fun set(logs: List<Entry>) {
    this.logs = logs.toMutableList()
    notifyDataSetChanged()
  }

  fun add(entry: Entry) {
    logs.add(entry)
    notifyDataSetChanged()
  }

  override fun getCount(): Int = logs.size

  override fun getItemId(position: Int): Long = position.toLong()

  override fun getItem(position: Int): Entry = logs[position]

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

    val view: View

    if (convertView != null) {
      view = convertView
    } else {
      view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_log, parent, false)
      view.tag = ViewHolder(view)
    }

    val entry: Entry = logs[position]
    val holder = view.tag as ViewHolder
    holder.levelView.text = entry.displayLevel()
    holder.tagView.text = entry.tag
    holder.messageView.text = entry.message

    val backgroundResource: Int = when (entry.level) {
      Log.VERBOSE, Log.DEBUG -> R.color.log_accent_debug
      Log.INFO -> R.color.log_accent_info
      Log.WARN -> R.color.log_accent_warn
      Log.ERROR, Log.ASSERT -> R.color.log_accent_error
      else -> R.color.log_accent_unknown
    }

    holder.view.setBackgroundResource(backgroundResource)

    return view
  }

  private class ViewHolder(val view: View) {
    val levelView: TextView = view.findViewById(R.id.debug_log_level)
    val tagView: TextView = view.findViewById(R.id.debug_log_tag)
    val messageView: TextView = view.findViewById(R.id.debug_log_message)
  }
}
