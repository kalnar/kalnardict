package eu.kalnarapps.kalnardict.data.database.external.gateways

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import eu.kalnarapps.kalnardict.common.operations.OperationException
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.database.external.DatabaseReaderContract
import eu.kalnarapps.kalnardict.data.database.external.SQLiteDbReaderHelper
import eu.kalnarapps.kalnardict.data.gateways.ExternalDatabaseGateway
import eu.kalnarapps.kalnardict.data.model.external.database.ExternalTranslationCreation

class DefaultExternalWordManager(
    private val context: Context
) : ExternalDatabaseGateway.WordManager {
    override suspend fun addWords(
        externalTranslationCreation: ExternalTranslationCreation
    ): OperationResult {

        return try {
            val dbHelper = SQLiteDbReaderHelper(context, externalTranslationCreation.dbPath)
            dbHelper.use { dbHelper ->
                dbHelper.writableDatabase.beginTransaction()
                for (entry in externalTranslationCreation.externalTranslationEntries) {
                    val contentValues = ContentValues().apply {
                        put(DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_ID, entry.id)
                        put(DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_BASE, entry.baseForm)
                        put(
                            DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_BASE_FORM_ALT,
                            entry.alternativeBaseForm
                        )
                        put(
                            DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_TRANSLATION,
                            entry.translation
                        )
                    }
                    dbHelper.writableDatabase.insertWithOnConflict(
                        externalTranslationCreation.tableName,
                        null,
                        contentValues,
                        SQLiteDatabase.CONFLICT_ROLLBACK
                    )
                }
                dbHelper.writableDatabase.endTransaction()
            }
            OperationResult.Success
        } catch (exception: Exception) {
            OperationResult.Failure(
                errorMessage = "SQLiteDbReaderHelper.writableDatabase.insertWithOnConflict " +
                        "failed with $externalTranslationCreation",
                cause = OperationException(exception)
            )
        }
    }
}
