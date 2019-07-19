package au.com.gridstone.debugdrawer.okhttplogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner
import au.com.gridstone.debugdrawer.DebugDrawerModule
import okhttp3.OkHttpClient
import timber.log.Timber

/**
 * A debug drawer module that can configure the level of logging that should be intercepted by
 * OkHttp and piped out to [Timber]. In order for this module to work the [HttpLogger] passed in to
 * this module must also be added as an interceptor to your [OkHttpClient].
 */
class OkHttpLoggerModule(private val httpLogger: HttpLogger) : DebugDrawerModule {

  override fun onCreateView(parent: ViewGroup): View {
    val inflater = LayoutInflater.from(parent.context)
    val view = inflater.inflate(R.layout.drawer_okhttp_logger, parent, false)
    val spinner: Spinner = view.findViewById(R.id.drawer_httpLogLevel)

    with(spinner) {
      adapter = HttpLogLevelAdapter()
      val initialPosition = httpLogger.getStoredLevelPosition()
      setSelection(initialPosition)

      onItemSelectedListener = object : OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
          httpLogger.setLevel(position)
        }
      }
    }

    return view
  }
}
