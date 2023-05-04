package unist.pjs.the.extends

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import java.io.*

fun Context.getBitmap(uri: Uri?): Bitmap? {
    var bitmap: Bitmap? = null

    uri?.apply {
        contentResolver.openInputStream(this)?.use { inputStream ->
            BufferedInputStream(inputStream).use { bitmap = BitmapFactory.decodeStream(it) }
        }
    }

    return bitmap
}

fun Bitmap.compress(tempFile: File): File? {
    try {
        FileOutputStream(tempFile).use {
            this.compress(Bitmap.CompressFormat.JPEG, 70, it)
        }
    } catch (ex: FileNotFoundException) {
        return null
    }
    return tempFile
}