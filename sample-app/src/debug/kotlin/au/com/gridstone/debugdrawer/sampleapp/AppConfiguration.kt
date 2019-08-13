package au.com.gridstone.debugdrawer.sampleapp

import android.app.Activity
import android.app.Application
import android.view.ViewGroup
import au.com.gridstone.debugdrawer.DebugDrawer
import au.com.gridstone.debugdrawer.DeviceInfoModule
import au.com.gridstone.debugdrawer.leakcanary.LeakCanaryModule
import au.com.gridstone.debugdrawer.okhttplogs.HttpLogger
import au.com.gridstone.debugdrawer.okhttplogs.OkHttpLoggerModule
import au.com.gridstone.debugdrawer.retrofit.DebugRetrofitConfig
import au.com.gridstone.debugdrawer.retrofit.Endpoint
import au.com.gridstone.debugdrawer.retrofit.RetrofitModule
import au.com.gridstone.debugdrawer.sampleapp.HttpConfiguration.API_URL
import au.com.gridstone.debugdrawer.timber.TimberModule
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior

/**
 * Debug configuration for the sample app.
 */
object AppConfiguration {

  private lateinit var app: Application

  private val endpoints = listOf(
      Endpoint("Mock", "http://localhost/mock/", isMock = true),
      Endpoint("Production", API_URL, isMock = false)
  )

  private val networkBehavior = NetworkBehavior.create()
  private val httpLogger by lazy { HttpLogger(app) }
  private val debugRetrofitConfig by lazy { DebugRetrofitConfig(app, endpoints, networkBehavior) }

  val api: GamesApi by lazy { createApi() }

  fun init(app: Application) {
    this.app = app
  }

  private fun createApi(): GamesApi {
    val currentEndpoint: Endpoint = debugRetrofitConfig.currentEndpoint
    val httpClient = HttpConfiguration.client.newBuilder()
        .addInterceptor(httpLogger.interceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(currentEndpoint.url)
        .client(httpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    if (currentEndpoint.isMock) {
      val mockRetrofit = MockRetrofit.Builder(retrofit).networkBehavior(networkBehavior).build()
      return MockGamesApi(mockRetrofit)
    }

    return retrofit.create<GamesApi>(GamesApi::class.java)
  }

  fun getRootViewContainerFor(activity: Activity): ViewGroup {
    return DebugDrawer.with(activity)
        .addSectionTitle("Network")
        .addModule(RetrofitModule(debugRetrofitConfig))
        .addSectionTitle("Logs")
        .addModule(OkHttpLoggerModule(httpLogger))
        .addModule(TimberModule())
        .addSectionTitle("Leaks")
        .addModule(LeakCanaryModule)
        .addSectionTitle("Device information")
        .addModule(DeviceInfoModule())
        .buildMainContainer()
  }
}
