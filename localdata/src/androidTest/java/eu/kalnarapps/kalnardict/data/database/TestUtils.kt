package eu.kalnarapps.kalnardict.data.database

import android.content.Context
import java.io.File


fun Context.createTestTempDir(): String {
    val dirPath = getStorageRootPath() + "/kalnardict/test/db"
    val dir = File(dirPath)
    val isCreated = dir.mkdirs()
    println(isCreated)
    return dirPath
}