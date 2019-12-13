package au.com.gridstone.debugdrawer.sampleapp

import android.app.Activity
import android.app.Application
import android.view.ViewGroup
import au.com.gridstone.debugdrawer.sampleapp.HttpConfiguration.API_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Release configuration for the sample app.
 */
object AppConfiguration {

  val api: GamesApi

  init {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    api = Retrofit.Builder()
        .baseUrl(API_URL)
        .client(HttpConfiguration.client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(GamesApi::class.java)
  }

  // Keep this method to match debug implementation.
  fun init(app: Application) {}

  fun getRootViewContainerFor(activity: Activity): ViewGroup {
    return activity.findViewById(android.R.id.content) as ViewGroup
  }
}
