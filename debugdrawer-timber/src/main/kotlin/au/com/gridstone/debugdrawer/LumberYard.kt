package au.com.gridstone.debugdrawer

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.ArrayDeque
import java.util.Calendar
import java.util.Deque
import java.util.Locale

object LumberYard {

  private const val BUFFER_SIZE = 200

  private val entries: Deque<Entry> = ArrayDeque(BUFFER_SIZE + 1)

  private lateinit var app: Application

  fun install(app: Application) {
    this.app = app

    cleanUp()

    Timber.plant(object : DebugTree() {
      override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        addEntry(Entry(priority, tag, message))
      }
    })
  }

  @Synchronized private fun addEntry(entry: Entry) {
    entries.addLast(entry)
    if (entries.size > BUFFER_SIZE) entries.removeFirst()
  }

  private fun save(callback: (File?) -> Unit) {
    SaveTask(callback).execute()
  }

  private fun cleanUp() {
    CleanUpTask.execute()
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

  private class SaveTask(private val callback: (File?) -> Unit) : AsyncTask<Unit, Unit, File?>() {
    override fun doInBackground(vararg params: Unit?): File? {
      val folder: File = app.getExternalFilesDir(null) ?: return null

      val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
      val timestamp: String = formatter.format(Calendar.getInstance().time)
      val filename = "$timestamp.log"

      val file = File(folder, filename)
      val bufferedEntries = ArrayList(entries)

      @Suppress("LiftReturnOrAssignment") // Keep things readable.
      try {
        val writer = BufferedWriter(FileWriter(file))

        for (entry in bufferedEntries) {
          writer.write(entry.prettyPrint())
          writer.newLine()
        }

        writer.close()

        return file
      } catch (e: IOException) {
        return null
      }
    }

    override fun onPostExecute(result: File?) {
      callback(result)
    }
  }

  private object CleanUpTask : AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg params: Unit?) {
      val folder: File? = app.getExternalFilesDir(null)
      val files: Array<File>? = folder?.listFiles()
      files?.filter { it.name.endsWith(".log") }?.forEach { it.delete() }
    }
  }
}
