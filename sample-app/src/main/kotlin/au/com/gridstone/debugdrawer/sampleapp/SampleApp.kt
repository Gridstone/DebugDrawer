package au.com.gridstone.debugdrawer.sampleapp

import android.app.Application
import com.squareup.leakcanary.LeakCanary

@Suppress("unused") // It's used in AndroidManifest.xml.
class SampleApp : Application() {
  override fun onCreate() {
    super.onCreate()

    if (LeakCanary.isInAnalyzerProcess(this)) return

    LeakCanary.install(this)
    AppConfiguration.init(this)
  }
}
