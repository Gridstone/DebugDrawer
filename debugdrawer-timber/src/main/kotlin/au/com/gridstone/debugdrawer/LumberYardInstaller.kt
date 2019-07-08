package au.com.gridstone.debugdrawer

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

class LumberYardInstaller : ContentProvider() {

  override fun onCreate(): Boolean {
    val app: Application = context?.applicationContext as? Application
                           ?: throw IllegalStateException("Could not obtain current Application.")

    LumberYard.install(app)
    return true
  }

  override fun insert(p0: Uri, p1: ContentValues?): Uri? = null
  override fun query(p0: Uri, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?): Cursor? = null
  override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int = 0
  override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int = 0
  override fun getType(p0: Uri): String? = null
}
