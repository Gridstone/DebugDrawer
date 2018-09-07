package au.com.gridstone.debugdrawer

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources

class LogsDialog(context: Context) : AlertDialog(context) {

  init {

    val resources: Resources = context.resources
    val title = resources.getString(R.string.drawer_logsTitle)
    val close = resources.getString(R.string.drawer_logsClose)
    val share = resources.getString(R.string.drawer_logsShare)
    val follow = resources.getString(R.string.drawer_logsFollow)

    setTitle(title)
    setButton(BUTTON_NEGATIVE, close) { _, _ -> /* No-op */ }
    setButton(BUTTON_POSITIVE, share) { _, _ -> share() }
    setButton(BUTTON_NEUTRAL, follow) { _, _ -> follow() }
  }

  private fun follow() {
    // TODO Implement.
  }

  private fun share() {
    // TODO Implement.
  }
}
