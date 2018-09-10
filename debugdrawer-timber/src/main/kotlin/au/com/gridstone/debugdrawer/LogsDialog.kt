package au.com.gridstone.debugdrawer

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.pm.ResolveInfo
import android.content.res.Resources
import android.net.Uri
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener
import android.widget.Button
import android.widget.ListView
import java.io.File

internal class LogsDialog(context: Context) : AlertDialog(context) {

  private val adapter = LogsAdapter()
  private val logsListView = ListView(context)

  init {
    val resources: Resources = context.resources
    val title = resources.getString(R.string.drawer_logsTitle)
    val close = resources.getString(R.string.drawer_logsClose)
    val share = resources.getString(R.string.drawer_logsShare)
    val follow = resources.getString(R.string.drawer_logsFollow)

    setTitle(title)
    setButton(BUTTON_NEGATIVE, close) { _, _ -> /* No-op */ }
    setButton(BUTTON_POSITIVE, share) { _, _ -> share() }
    setButton(BUTTON_NEUTRAL, follow, null as OnClickListener?)

    logsListView.adapter = adapter
    logsListView.transcriptMode = ListView.TRANSCRIPT_MODE_NORMAL
    logsListView.isStackFromBottom = true
    setView(logsListView)
  }

  override fun onAttachedToWindow() {
    adapter.set(LumberYard.getEntries())
    LumberYard.setListener { entry -> adapter.add(entry) }

    // We need to set our neutral button click listener manually like this to avoid having it hide
    // the dialog.
    val followButton: Button = getButton(BUTTON_NEUTRAL)
    followButton.setOnClickListener {
      logsListView.setSelection(adapter.count - 1)
    }

    logsListView.setOnScrollListener(object : OnScrollListener {

      override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int,
                            totalItemCount: Int) {
        followButton.isEnabled = logsListView.lastVisiblePosition != adapter.count - 1
      }

      override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {}
    })
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    LumberYard.clearListener()
    logsListView.setOnScrollListener(null)
    getButton(BUTTON_NEUTRAL).setOnClickListener(null)
  }

  private fun share() {
    LumberYard.save { file: File? ->
      // We couldn't produce a logs file for some reason. Give up.
      if (file == null) return@save

      val sendIntent = Intent(ACTION_SEND)
      sendIntent.type = "text/plain"
      sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))

      val handlers: List<ResolveInfo> = context.packageManager.queryIntentActivities(sendIntent, 0)
      // Give up if our send intent cannot be handled.
      if (handlers.isEmpty()) return@save

      TODO("Implement file sharing.")
    }
  }
}
