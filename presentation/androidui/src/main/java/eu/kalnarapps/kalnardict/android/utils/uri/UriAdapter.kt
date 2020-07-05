package eu.kalnarapps.kalnardict.android.utils.uri

import android.net.Uri

class UriAdapter {
    fun convertUriToSdcardPath(uri: Uri?): String {
        return uri?.lastPathSegment?.split(":")?.get(1).orEmpty()
    }
}