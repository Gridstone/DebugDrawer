package au.com.gridstone.debugdrawer.okhttplogs

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import okhttp3.logging.HttpLoggingInterceptor.Logger
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber

/**
 * A wrapper around an OkHttp logging interceptor that pipes logs out to [Timber]. The log level is
 * dictated by [OkHttpLoggerModule] in the debug drawer.
 *
 * You must add [interceptor] to your client via [OkHttpClient.Builder.addInterceptor] in order for
 * logs to be intercepted and delivered to Timber.
 */
class HttpLogger(context: Context, prettyPrintJson: Boolean = true) {

  private val logInterceptor = HttpLoggingInterceptor(object : Logger {
    override fun log(message: String) {
      val formattedMessage: String = try {
        when {
          !prettyPrintJson -> message
          message.startsWith('{') -> JSONObject(message).toString(2)
          message.startsWith('[') -> JSONArray(message).toString(2)
          else -> message
        }
      } catch (e: JSONException) {
        message
      }

      Timber.tag("HTTP").v(formattedMessage)
    }
  })

  private val sharedPrefs: SharedPreferences = context
      .getSharedPreferences("DebugDrawer_HttpLogger", MODE_PRIVATE)

  /**
   * An interceptor that pipes HTTP
   */
  val interceptor: Interceptor = logInterceptor

  init {
    val storedLevel = sharedPrefs.getInt(KEY_LOG_LEVEL, 0)
    logInterceptor.level = Level.values()[storedLevel]
  }

  internal fun getStoredLevelPosition(): Int = sharedPrefs.getInt(KEY_LOG_LEVEL, 0)

  internal fun setLevel(position: Int) {
    sharedPrefs.edit().putInt(KEY_LOG_LEVEL, position).apply()
    logInterceptor.level = Level.values()[position]
  }
}

private const val KEY_LOG_LEVEL = "logLevel"
