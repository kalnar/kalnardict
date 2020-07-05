package eu.kalnarapps.kalnardict.data.database

import android.content.Context
import eu.kalnarapps.kalnardict.data.database.inapp.getStorageRootPath
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream


fun Context.createTestTempDir(): String {
    val dirPath = getStorageRootPath() + TEST_TEMP_DIR_LOCAL_PATH
    val dir = File(dirPath)
    val isCreated = dir.mkdirs()
    println(isCreated)
    return dirPath
}

const val TEST_TEMP_DIR_LOCAL_PATH = ".kalnardict/test/db"
const val EXTERNAL_TEST_DB_NAME = "test_external.db"
const val INVALID_EXTERNAL_TEST_DB_NAME = "test_invalid_external.db"
const val TEST_DICT_TABLE_NAME = "test_fr_dictionary"

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

fun Context.copyInvalidTestDbFromAssetsToTempTestDir() {

    val initialStream: InputStream = assets.open("database/$INVALID_EXTERNAL_TEST_DB_NAME")
    val buffer = ByteArray(initialStream.available())
    initialStream.read(buffer)

    val targetFile = File("${createTestTempDir()}/$EXTERNAL_TEST_DB_NAME")
    val outStream: OutputStream = FileOutputStream(targetFile)
    outStream.write(buffer)
    outStream.close()
    initialStream.close()
}
