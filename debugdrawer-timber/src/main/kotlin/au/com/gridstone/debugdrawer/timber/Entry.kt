package au.com.gridstone.debugdrawer.timber

import android.util.Log

internal class Entry(val level: Int, val tag: String?, val message: String) {
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
