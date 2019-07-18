package au.com.gridstone.debugdrawer.retrofit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

internal class NetworkEndpointAdapter(private val endpoints: List<Endpoint>,
                                      context: Context) : BindableAdapter<Endpoint>(context) {

  override fun getCount(): Int = endpoints.size

  override fun getItemId(position: Int): Long = position.toLong()

  override fun getItem(position: Int): Endpoint = endpoints[position]

  override fun newView(inflater: LayoutInflater, position: Int, container: ViewGroup): View =
      inflater.inflate(android.R.layout.simple_spinner_item, container, false)

  override fun bindView(item: Endpoint, position: Int, view: View) {
    val textView: TextView = view.findViewById(android.R.id.text1)
    textView.text = item.name
  }

  override fun newDropDownView(inflater: LayoutInflater, position: Int,
                               container: ViewGroup): View =
      inflater.inflate(android.R.layout.simple_spinner_dropdown_item, container, false)
}
