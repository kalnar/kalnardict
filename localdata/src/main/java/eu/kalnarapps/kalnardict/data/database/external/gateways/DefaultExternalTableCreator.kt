package eu.kalnarapps.kalnardict.data.database.external.gateways

import android.content.Context
import eu.kalnarapps.kalnardict.common.operations.OperationException
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.database.external.SQLiteDbReaderHelper
import eu.kalnarapps.kalnardict.data.database.external.insertMetaInfo
import eu.kalnarapps.kalnardict.data.database.external.sqlCreateTable
import eu.kalnarapps.kalnardict.data.gateways.ExternalDatabaseGateway

class DefaultExternalTableCreator(
    private val context: Context
) : ExternalDatabaseGateway.TableManager {

    override suspend fun createEmptyTable(
        path: String,
        tableName: String,
        sourceLanguage: String,
        targetLanguage: String
    ): OperationResult {

        val createTableSql = sqlCreateTable(tableName)
        val insertMetaInfoSql = insertMetaInfo(
            name = tableName,
            languageFrom = sourceLanguage,
            languageTo = targetLanguage
        )
        try {
            val dbHelper = SQLiteDbReaderHelper(context, path)
            dbHelper.use { dbHelper ->
                dbHelper.writableDatabase.execSQL(createTableSql)
                dbHelper.writableDatabase.execSQL(insertMetaInfoSql)
            }
        } catch (exception: Exception) {
            return OperationResult.Failure(
                errorMessage = "SQLiteDbReaderHelper.writableDatabase.execSQL failed on path: " +
                        "$path with create table or insert meta: " +
                        "$createTableSql\n$insertMetaInfoSql",
                cause = OperationException(exception)
            )
        }
        return OperationResult.Success
    }

    override suspend fun deleteTable(path: String, tableName: String): OperationResult {
        return OperationResult.Success
    }
}