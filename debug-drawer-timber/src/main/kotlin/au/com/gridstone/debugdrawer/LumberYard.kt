package au.com.gridstone.debugdrawer

import android.app.Application
import android.util.Log
import timber.log.Timber
import timber.log.Timber.DebugTree

object LumberYard {

  private lateinit var app: Application

  fun install(app: Application) {
    this.app = app

    Timber.plant(object : DebugTree() {
      override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        addEntry(Entry(priority, tag, message))
      }
    })
  }

  @Synchronized private fun addEntry(entry: Entry) {
    TODO("Implement method.")
  }

  private class Entry(val level: Int, val tag: String?, val message: String) {
    // Indent newlines to match the original indentation.
    fun prettyPrint() = "%22s %s %s".format(tag, displayLevel(),
                                            message.replace("\\n".toRegex(),
                                                            "\n                         "))

    fun displayLevel() = when (level) {
      Log.VERBOSE -> "V"
      Log.DEBUG -> "D"
      Log.INFO -> "I"
      Log.WARN -> "W"
      Log.ERROR -> "E"
      Log.ASSERT -> "A"
      else -> "?"
    }
  }
}
