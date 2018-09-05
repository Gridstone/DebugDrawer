package au.com.gridstone.debugdrawer

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.jakewharton.processphoenix.ProcessPhoenix
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit.MILLISECONDS

class DebugRetrofitConfig(context: Context,
                          internal val endpoints: List<Endpoint>,
                          private val networkBehavior: NetworkBehavior) {

  private val appContext = context.applicationContext

  private val sharedPrefs: SharedPreferences =
      appContext.getSharedPreferences("DebugDrawer_Retrofit", MODE_PRIVATE)

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

  internal var endpoint: Endpoint
    get() = currentEndpoint
    set(value) {
      sharedPrefs.putBlocking(KEY_ENDPOINT, value.name)
      ProcessPhoenix.triggerRebirth(appContext)
    }

  internal var delayMs: Long
    get() = networkBehavior.delay(MILLISECONDS)
    set(value) {
      sharedPrefs.put(KEY_DELAY, value)
      networkBehavior.setDelay(value, MILLISECONDS)
    }

  internal var variancePercent: Int
    get() = networkBehavior.variancePercent()
    set(value) {
      sharedPrefs.put(KEY_VARIANCE, value)
      networkBehavior.setVariancePercent(value)
    }

  internal var failurePercent: Int
    get() = networkBehavior.failurePercent()
    set(value) {
      sharedPrefs.put(KEY_FAILURE_PERCENT, value)
      networkBehavior.setFailurePercent(value)
    }

  internal var errorPercent: Int
    get() = networkBehavior.errorPercent()
    set(value) {
      sharedPrefs.put(KEY_ERROR_PERCENT, value)
      networkBehavior.setErrorPercent(value)
    }

  internal var errorCode: Int
    get() = sharedPrefs.getInt(KEY_ERROR_CODE, DEFAULT_ERROR_CODE)
    set(value) {
      sharedPrefs.put(KEY_ERROR_CODE, value)
      // No need to update error factory since it references sharedPrefs directly.
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
