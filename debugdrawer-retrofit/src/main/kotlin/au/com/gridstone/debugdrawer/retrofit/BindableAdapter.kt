package au.com.gridstone.debugdrawer.retrofit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

/**
 * An implementation of [BaseAdapter] which uses the new/bind pattern for its views.
 */
internal abstract class BindableAdapter<T>(context: Context) : BaseAdapter() {
  private val inflater = LayoutInflater.from(context)

  abstract override fun getItem(position: Int): T

  override fun getView(position: Int, view: View?, container: ViewGroup): View {
    val reifiedView = view ?: newView(inflater, position, container)
    bindView(getItem(position), position, reifiedView)
    return reifiedView
  }

  /**
   * Create a new instance of a view for the specified position.
   * */
  abstract fun newView(inflater: LayoutInflater, position: Int, container: ViewGroup): View

  /**
   * Bind the data for the specified `position` to the view.
   */
  abstract fun bindView(item: T, position: Int, view: View)

  override fun getDropDownView(position: Int, view: View?, container: ViewGroup): View {
    val reifiedView = view ?: newDropDownView(inflater, position, container)
    bindDropDownView(getItem(position), position, reifiedView)
    return reifiedView
  }

  /**
   * Create a new instance of a drop-down view for the specified position.
   */
  open fun newDropDownView(inflater: LayoutInflater, position: Int, container: ViewGroup): View =
      newView(inflater, position, container)

  /**
   * Bind the data for the specified `position` to the drop-down view.
   */
  open fun bindDropDownView(item: T, position: Int, view: View) {
    bindView(item, position, view)
  }
}
