package au.com.gridstone.debugdrawer.sampleapp

import android.app.Activity
import android.app.Application
import android.view.ViewGroup
import au.com.gridstone.debugdrawer.sampleapp.HttpConfiguration.API_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Release configuration for the sample app.
 */
object AppConfiguration {

  val api: GamesApi = Retrofit.Builder()
      .baseUrl(API_URL)
      .client(HttpConfiguration.client)
      .addConverterFactory(MoshiConverterFactory.create())
      .addCallAdapterFactory(CoroutineCallAdapterFactory())
      .build()
      .create(GamesApi::class.java)

  // Keep this method to match debug implementation.
  fun init(app: Application) {}

  fun getRootViewContainerFor(activity: Activity): ViewGroup {
    return activity.findViewById(android.R.id.content) as ViewGroup
  }
}
