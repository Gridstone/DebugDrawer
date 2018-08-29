package au.com.gridstone.debugdrawer

import android.content.res.Resources
import android.view.View

internal fun Resources.dpToPx(dp: Int) = (dp * displayMetrics.density).toInt()
internal fun View.dpToPx(dp: Int): Int = resources.dpToPx(dp)
