package eu.kalnarapps.kalnardict.android.utils.uri

import android.content.Context
import android.net.Uri
import eu.kalnarapps.kalnardict.common.utils.getStorageRootPath
import java.io.File

class UriAdapter(
    private val context: Context
) {
    fun convertUriToSdcardPath(uri: Uri?): String {
        val path = uri?.lastPathSegment?.split(":")?.get(1).orEmpty()
        val file = File(path)
        return if (file.exists()) {
            path
        } else {
            "${context.getStorageRootPath()}/$path"
        }
    }
}