package eu.kalnarapps.kalnardict.permissions

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHandler {

    companion object {
        private const val MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE: Int = 0

        fun Activity.requestPermissionsIfNeeded() {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
                )

            }
        }
    }
}
