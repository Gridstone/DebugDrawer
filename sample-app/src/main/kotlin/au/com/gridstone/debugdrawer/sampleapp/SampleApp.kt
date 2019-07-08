package au.com.gridstone.debugdrawer.sampleapp

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

@Suppress("unused") // It's used in AndroidManifest.xml.
class SampleApp : Application() {
  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) Timber.plant(DebugTree())

    AppConfiguration.init(this)
  }
}
