package unist.pjs.the.extends

import android.text.TextUtils
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String?) {
    context?.let {
        if (!TextUtils.isEmpty(message))
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
    }
}
