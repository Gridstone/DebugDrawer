package au.com.gridstone.debugdrawer

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import com.jakewharton.processphoenix.ProcessPhoenix
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit.MILLISECONDS

class RetrofitModule(context: Context,
                     private val endpoints: List<Endpoint>,
                     private val networkBehavior: NetworkBehavior) : DebugDrawerModule {

  private val sharedPrefs: SharedPreferences =
      context.getSharedPreferences("DebugDrawer_Retrofit", MODE_PRIVATE)

  val currentEndpoint: Endpoint

  init {
    val persistedEndpointName: String? = sharedPrefs.getString(KEY_ENDPOINT, null)
    currentEndpoint = endpoints.firstOrNull { it.name == persistedEndpointName } ?: endpoints.first()

    networkBehavior.setDelay(sharedPrefs.getLong(KEY_DELAY, DEFAULT_DELAY), MILLISECONDS)
    networkBehavior.setVariancePercent(sharedPrefs.getInt(KEY_VARIANCE, DEFAULT_VARIANCE))
    networkBehavior.setFailurePercent(
        sharedPrefs.getInt(KEY_FAILURE_PERCENT, DEFAULT_FAILURE_PERCENT))
    networkBehavior.setErrorPercent(sharedPrefs.getInt(KEY_ERROR_PERCENT, DEFAULT_ERROR_PERCENT))
    networkBehavior.setErrorFactory(ErrorFactory(sharedPrefs))
  }

  override fun onCreateView(parent: ViewGroup): View {
    val inflater = LayoutInflater.from(parent.context)
    val view: View = inflater.inflate(R.layout.drawer_retrofit, parent, false)

    // Set up endpoint spinner.

    val endpointSpinner: Spinner = view.findViewById(R.id.drawer_retrofitEndpoint)
    with(endpointSpinner) {
      val endpointAdapter = NetworkEndpointAdapter(endpoints, parent.context)
      val initialPos: Int = endpoints.indexOf(currentEndpoint)

      adapter = endpointAdapter
      setSelection(initialPos)

      onItemSelected { position ->
        val endpoint = endpoints[position]
        sharedPrefs.putBlocking(KEY_ENDPOINT, endpoint.name)
        ProcessPhoenix.triggerRebirth(parent.context)
      }
    }

    // Set up network delay spinner.

    val delaySpinner: Spinner = view.findViewById(R.id.drawer_retrofitDelay)
    with(delaySpinner) {
      val delayAdapter = NetworkDelayAdapter(parent.context)
      val initialPos: Int = delayAdapter.getPositionForValue(networkBehavior.delay(MILLISECONDS))

      adapter = delayAdapter
      isEnabled = currentEndpoint.isMock
      setSelection(initialPos)

      onItemSelected { position ->
        val delayMs: Long = delayAdapter.getItem(position)
        sharedPrefs.put(KEY_DELAY, delayMs)
        networkBehavior.setDelay(delayMs, MILLISECONDS)
      }
    }

    // Set up variance spinner.

    val varianceSpinner: Spinner = view.findViewById(R.id.drawer_retrofitVariance)
    with(varianceSpinner) {
      val varianceAdapter = NetworkVarianceAdapter(parent.context)
      val initialPos: Int = varianceAdapter.getPositionForValue(networkBehavior.variancePercent())

      adapter = varianceAdapter
      isEnabled = currentEndpoint.isMock
      setSelection(initialPos)

      onItemSelected { position ->
        val percent: Int = varianceAdapter.getItem(position)
        sharedPrefs.put(KEY_VARIANCE, percent)
        networkBehavior.setVariancePercent(percent)
      }
    }

    // Set up failure rate spinner.

    val failureRateSpinner: Spinner = view.findViewById(R.id.drawer_retrofitFailureRate)
    with(failureRateSpinner) {
      val failureRateAdapter = NetworkPercentageAdapter(parent.context)
      val initialPos: Int = failureRateAdapter.getPositionForValue(networkBehavior.failurePercent())

      adapter = failureRateAdapter
      isEnabled = currentEndpoint.isMock
      setSelection(initialPos)

      onItemSelected { position ->
        val percent: Int = failureRateAdapter.getItem(position)
        sharedPrefs.put(KEY_FAILURE_PERCENT, percent)
        networkBehavior.setFailurePercent(percent)
      }
    }

    // Set up error rate spinner.

    val errorRateSpinner: Spinner = view.findViewById(R.id.drawer_retrofitErrorRate)
    with(errorRateSpinner) {
      val errorRateAdapter = NetworkPercentageAdapter(parent.context)
      val initialPos: Int = errorRateAdapter.getPositionForValue(networkBehavior.errorPercent())

      adapter = errorRateAdapter
      isEnabled = currentEndpoint.isMock
      setSelection(initialPos)

      onItemSelected { position ->
        val percent: Int = errorRateAdapter.getItem(position)
        sharedPrefs.put(KEY_ERROR_PERCENT, percent)
        networkBehavior.setErrorPercent(percent)
      }
    }

    // Set up error type spinner.

    val errorCodeSpinner: Spinner = view.findViewById(R.id.drawer_retrofitErrorCode)
    with(errorCodeSpinner) {
      val errorCodeAdapter = NetworkErrorCodeAdapter(parent.context)
      val currentErrorCode = sharedPrefs.getInt(KEY_ERROR_CODE, DEFAULT_ERROR_CODE)
      val initialPos: Int = errorCodeAdapter.getPositionForValue(currentErrorCode)

      adapter = errorCodeAdapter
      isEnabled = currentEndpoint.isMock
      setSelection(initialPos)

      onItemSelected { position ->
        val code: Int = errorCodeAdapter.getItem(position)
        sharedPrefs.put(KEY_ERROR_CODE, code)
        // No need to update error factory since it references sharedPrefs directly.
      }
    }

    return view
  }

  private class ErrorFactory(
      private val sharedPrefs: SharedPreferences) : Callable<Response<*>> {

    override fun call(): Response<Any?> {
      val errorCode: Int = sharedPrefs.getInt(KEY_ERROR_CODE, DEFAULT_ERROR_CODE)
      return Response.error(errorCode, ResponseBody.create(null, ByteArray(0)))
    }
  }
}

private const val KEY_ENDPOINT = "endpoint"
private const val KEY_DELAY = "delay"
private const val KEY_VARIANCE = "variance"
private const val KEY_FAILURE_PERCENT = "failurePercent"
private const val KEY_ERROR_PERCENT = "errorPercent"
private const val KEY_ERROR_CODE = "errorCode"
private const val DEFAULT_DELAY = 1000L
private const val DEFAULT_VARIANCE = 40
private const val DEFAULT_FAILURE_PERCENT = 0
private const val DEFAULT_ERROR_PERCENT = 0
private const val DEFAULT_ERROR_CODE = 500
