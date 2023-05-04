package unist.pjs.the.extends

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.showPermissionSnackBar(view: View?, @StringRes id: Int) {
    val context = activity?.applicationContext

    context?.let { applicationContext ->
        view?.let { view ->
            Snackbar.make(view, applicationContext.getString(id), Snackbar.LENGTH_SHORT).apply {
                setAction("확인") {
                    applicationContext.startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + applicationContext.packageName)
                        ).apply {
                            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    )
                }
            }.show()
        }
    }
}