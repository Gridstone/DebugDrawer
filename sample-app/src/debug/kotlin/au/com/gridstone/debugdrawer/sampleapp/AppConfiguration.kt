package au.com.gridstone.debugdrawer.sampleapp

import android.app.Activity
import android.app.Application
import android.view.ViewGroup
import au.com.gridstone.debugdrawer.DebugDrawer
import au.com.gridstone.debugdrawer.DebugRetrofitConfig
import au.com.gridstone.debugdrawer.DeviceInfoModule
import au.com.gridstone.debugdrawer.Endpoint
import au.com.gridstone.debugdrawer.LeakCanaryModule
import au.com.gridstone.debugdrawer.RetrofitModule
import au.com.gridstone.debugdrawer.TimberModule
import au.com.gridstone.debugdrawer.sampleapp.HttpConfiguration.API_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior

object AppConfiguration {

  private lateinit var app: Application

  private val endpoints = listOf(
      Endpoint("Mock", "http://localhost/mock/", isMock = true),
      Endpoint("Production", API_URL, isMock = false)
  )

  private val networkBehavior = NetworkBehavior.create()
  private val debugRetrofitConfig by lazy { DebugRetrofitConfig(app, endpoints, networkBehavior) }

  val api: GamesApi by lazy { createApi() }

  fun init(app: Application) {
    this.app = app
  }

  private fun createApi(): GamesApi {
    val currentEndpoint: Endpoint = debugRetrofitConfig.currentEndpoint
    val retrofit = Retrofit.Builder()
        .baseUrl(currentEndpoint.url)
        .client(HttpConfiguration.client)
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
        .addModule(TimberModule())
        .addModule(LeakCanaryModule())
        .addSectionTitle("Device information")
        .addModule(DeviceInfoModule())
        .finishAndGetMainContainer()
  }
}
