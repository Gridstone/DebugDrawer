package au.com.gridstone.debugdrawer.okhttplogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import okhttp3.logging.HttpLoggingInterceptor.Level

internal class HttpLogLevelAdapter : BaseAdapter() {

  override fun getCount(): Int = Level.values().size

  override fun getItemId(position: Int): Long = position.toLong()

  override fun getItem(position: Int): Level = Level.values()[position]

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val view: TextView

    @Suppress("LiftReturnOrAssignment") // Keep things readable.
    if (convertView != null) {
      view = convertView as TextView
    } else {
      val inflater: LayoutInflater = LayoutInflater.from(parent.context)
      view = inflater.inflate(android.R.layout.simple_spinner_item, parent, false) as TextView
    }

    view.text = Level.values()[position].name
    return view
  }

  override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
    val view: TextView

    @Suppress("LiftReturnOrAssignment") // Keep things readable.
    if (convertView != null) {
      view = convertView as TextView
    } else {
      val inflater: LayoutInflater = LayoutInflater.from(parent.context)
      view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent,
                              false) as TextView
    }

    view.text = Level.values()[position].name
    return view
  }
}
