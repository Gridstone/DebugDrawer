package au.com.gridstone.debugdrawer

import android.app.Application

/**
 * A no-op version of LumberYard to be used in release builds.
 */
object LumberYard {

  /*
   * Normally sets up Timber logs to be piped into a file to be displayed from the debug drawer, but
   * this implementation does nothing.
   */
  fun install(app: Application) {}
}
