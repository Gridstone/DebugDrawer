package au.com.gridstone.debugdrawer.sampleapp

import android.app.Application
import au.com.gridstone.debugdrawer.LumberYard
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber
import timber.log.Timber.DebugTree

@Suppress("unused") // It's used in AndroidManifest.xml.
class SampleApp : Application() {
  override fun onCreate() {
    super.onCreate()

    if (LeakCanary.isInAnalyzerProcess(this)) return

    if (BuildConfig.DEBUG) Timber.plant(DebugTree())

    LeakCanary.install(this)
    LumberYard.install(this)
    AppConfiguration.init(this)
  }
}
