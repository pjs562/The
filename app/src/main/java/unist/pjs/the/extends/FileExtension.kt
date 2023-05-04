package unist.pjs.the.extends

import androidx.fragment.app.Fragment
import java.io.File

fun Fragment.createTempFile(fileName: String? = null): File {
    val mFolder = File("${requireContext().filesDir}/Images")

    if (!mFolder.exists()) {
        mFolder.mkdir()
    }

    val tempFile = File(
        mFolder.absolutePath,
        if (fileName.isNullOrBlank()) "IMG_${Preferences.userAge}${System.currentTimeMillis()}.jpg" else fileName
    )
    tempFile.createNewFile()

    return tempFile
}