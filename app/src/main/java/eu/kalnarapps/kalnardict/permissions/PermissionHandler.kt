package eu.kalnarapps.kalnardict.permissions

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class PermissionHandler {

    companion object {
        private const val MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE: Int = 0

        @RequiresApi(Build.VERSION_CODES.M)
        fun Activity.requestPermissionsIfNeeded() {
            if (arrayOf(
                    WRITE_EXTERNAL_STORAGE,
                    READ_EXTERNAL_STORAGE
                ).any {
                    ContextCompat.checkSelfPermission(
                        this,
                        it
                    ) != PackageManager.PERMISSION_GRANTED
                }
            ) {
                requestPermissions(
                    arrayOf(
                        WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE
                    ),
                    MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
                )

            }
        }
    }
}
