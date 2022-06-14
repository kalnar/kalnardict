package eu.kalnarapps.kalnardict.data.database.external.gateways

import android.content.Context
import eu.kalnarapps.kalnardict.common.operations.OperationException
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.database.external.SQL_CREATE_META_INFO
import eu.kalnarapps.kalnardict.data.database.external.SQLiteDbReaderHelper
import eu.kalnarapps.kalnardict.data.gateways.ExternalDatabaseGateway
import java.io.File
import java.lang.Exception

class DefaultExternalDatabaseGateway(
    private val context: Context
) : ExternalDatabaseGateway {
    override suspend fun createDatabase(path: String): OperationResult {

        try {

            File(File(path).parent).mkdirs()

            val dbHelper = SQLiteDbReaderHelper(context, path)
            dbHelper.use { dbHelper ->
                dbHelper.writableDatabase.execSQL(SQL_CREATE_META_INFO)
            }
        } catch (exception: Exception) {
            return OperationResult.Failure(
                errorMessage = "dbHelper.writableDatabase.execSQL(SQL_CREATE_META_INFO) failed on path: $path",
                cause = OperationException(exception)
            )
        }


        return OperationResult.Success
    }
}