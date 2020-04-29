package eu.kalnarapps.kalnardict.data.database

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream


fun Context.createTestTempDir(): String {
    val dirPath = getStorageRootPath() + ".kalnardict/test/db"
    val dir = File(dirPath)
    val isCreated = dir.mkdirs()
    println(isCreated)
    return dirPath
}

const val EXTERNAL_TEST_DB_NAME = "test_external.db"

fun Context.copyTestDbFromAssetsToTempTestDir() {

    val initialStream: InputStream = assets.open("database/$EXTERNAL_TEST_DB_NAME")
    val buffer = ByteArray(initialStream.available())
    initialStream.read(buffer)

    val targetFile = File("${createTestTempDir()}/$EXTERNAL_TEST_DB_NAME")
    val outStream: OutputStream = FileOutputStream(targetFile)
    outStream.write(buffer)
    outStream.close()
    initialStream.close()
}