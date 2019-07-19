package au.com.gridstone.debugdrawer.retrofit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

internal class NetworkDelayAdapter(context: Context) : BindableAdapter<Long>(context) {

  private val values = longArrayOf(250, 500, 1000, 2000, 3000, 5000)

  fun getPositionForValue(value: Long): Int {
    val position = values.indexOf(value)
    if (position >= 0) return position
    return 2 // Default to 1000 if something changes.
  }

  override fun getCount(): Int = values.size

  override fun getItemId(position: Int): Long = position.toLong()

  override fun getItem(position: Int): Long = values[position]

  override fun newView(inflater: LayoutInflater, position: Int, container: ViewGroup): View =
      inflater.inflate(android.R.layout.simple_spinner_item, container, false)

  override fun bindView(item: Long, position: Int, view: View) {
    val textView = view.findViewById<TextView>(android.R.id.text1)
    textView.text = view.context.getString(R.string.drawer_retrofitDelayEntry, item)
  }

  override fun newDropDownView(inflater: LayoutInflater, position: Int,
                               container: ViewGroup): View =
      inflater.inflate(android.R.layout.simple_spinner_dropdown_item, container, false)
}
