package au.com.gridstone.debugdrawer.retrofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import au.com.gridstone.debugdrawer.DebugDrawerModule
import retrofit2.Retrofit
import retrofit2.mock.NetworkBehavior

/**
 * Uses a [DebugRetrofitConfig] to display controls to modify the behaviour of
 * [Retrofit]/[NetworkBehavior].
 *
 * - **Endpoint:** Allows the URL that `Retrofit` uses  to be changed. In addition, if the selected
 *   [Endpoint] is listed as a mock endpoint then the other controls in this module will be enabled.
 *
 * - **Delay:** How long a mock request should take.
 *
 * - **Variance:** How much variance in time there should be for mock requests.
 *
 * - **Fail rate:** The percentage of requests that should fail. Failures represent problems with a
 *   network transaction, distinct from errors received from a server.
 *
 * - **Error rate:** The percentage of requests that should return an error.
 *
 * - **Error code:** The error code that is returned for requests that return mock errors.
 */
class RetrofitModule(private val config: DebugRetrofitConfig) : DebugDrawerModule {

  override fun onCreateView(parent: ViewGroup): View {
    val inflater = LayoutInflater.from(parent.context)
    val view: View = inflater.inflate(R.layout.drawer_retrofit, parent, false)

    // Set up endpoint spinner.

    val endpointSpinner: Spinner = view.findViewById(R.id.drawer_retrofitEndpoint)
    with(endpointSpinner) {
      val endpointAdapter = NetworkEndpointAdapter(config.endpoints, parent.context)
      val initialPos: Int = config.endpoints.indexOf(config.currentEndpoint)

      adapter = endpointAdapter
      setSelection(initialPos)

      onItemSelected { position ->
        config.setEndpointAndRelaunch(config.endpoints[position])
      }
    }

    // Set up network delay spinner.

    val delaySpinner: Spinner = view.findViewById(R.id.drawer_retrofitDelay)
    with(delaySpinner) {
      val delayAdapter = NetworkDelayAdapter(parent.context)
      val initialPos: Int = delayAdapter.getPositionForValue(config.delayMs)

      adapter = delayAdapter
      isEnabled = config.currentEndpoint.isMock
      setSelection(initialPos)

      onItemSelected { position ->
        config.delayMs = delayAdapter.getItem(position)
      }
    }

    // Set up variance spinner.

    val varianceSpinner: Spinner = view.findViewById(R.id.drawer_retrofitVariance)
    with(varianceSpinner) {
      val varianceAdapter = NetworkVarianceAdapter(parent.context)
      val initialPos: Int = varianceAdapter.getPositionForValue(config.variancePercent)

      adapter = varianceAdapter
      isEnabled = config.currentEndpoint.isMock
      setSelection(initialPos)

      onItemSelected { position ->
        config.variancePercent = varianceAdapter.getItem(position)
      }
    }

    // Set up failure rate spinner.

    val failureRateSpinner: Spinner = view.findViewById(R.id.drawer_retrofitFailureRate)
    with(failureRateSpinner) {
      val failureRateAdapter = NetworkPercentageAdapter(parent.context)
      val initialPos: Int = failureRateAdapter.getPositionForValue(config.failurePercent)

      adapter = failureRateAdapter
      isEnabled = config.currentEndpoint.isMock
      setSelection(initialPos)

      onItemSelected { position ->
        config.failurePercent = failureRateAdapter.getItem(position)
      }
    }

    // Set up error rate spinner.

    val errorRateSpinner: Spinner = view.findViewById(R.id.drawer_retrofitErrorRate)
    with(errorRateSpinner) {
      val errorRateAdapter = NetworkPercentageAdapter(parent.context)
      val initialPos: Int = errorRateAdapter.getPositionForValue(config.errorPercent)

      adapter = errorRateAdapter
      isEnabled = config.currentEndpoint.isMock
      setSelection(initialPos)

      onItemSelected { position ->
        config.errorPercent = errorRateAdapter.getItem(position)
      }
    }

    // Set up error type spinner.

    val errorCodeSpinner: Spinner = view.findViewById(R.id.drawer_retrofitErrorCode)
    with(errorCodeSpinner) {
      val errorCodeAdapter = NetworkErrorCodeAdapter(parent.context)
      val initialPos: Int = errorCodeAdapter.getPositionForValue(config.errorCode)

      adapter = errorCodeAdapter
      isEnabled = config.currentEndpoint.isMock
      setSelection(initialPos)

      onItemSelected { position ->
        config.errorCode = errorCodeAdapter.getItem(position)
      }
    }

    return view
  }
}
