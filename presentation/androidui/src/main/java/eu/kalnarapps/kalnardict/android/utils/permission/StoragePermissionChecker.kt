package eu.kalnarapps.kalnardict.android.utils.permission

import android.os.Build
import android.os.Environment

class StoragePermissionChecker: StoragePermissionCheckerContract {

    override fun hasManageExternalStoragePermission(): Boolean {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || Environment.isExternalStorageManager())
    }
}