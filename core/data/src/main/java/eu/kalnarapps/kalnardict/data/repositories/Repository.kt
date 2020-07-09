package eu.kalnarapps.kalnardict.data.repositories

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.data.ExternalDatabaseHandler
import eu.kalnarapps.kalnardict.data.ExternalDictionaryResource
import eu.kalnarapps.kalnardict.data.ImportEntry
import eu.kalnarapps.kalnardict.data.dao.DictDao
import eu.kalnarapps.kalnardict.data.mapper.DictEntry
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.NewDictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.toExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictTranslation
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord
import java.util.Locale

class Repository(
    private val dictDao: DictDao,
    private val externalDbHandler: ExternalDatabaseHandler
) : DictionaryRepository {

    override suspend fun insertDictEntry(dictTranslation: DictTranslation): OperationResult {
        // TODO return failure if dictionary id is not correct in translation
        dictDao.insertDictEntry(
            dictTranslation.toDictEntry()
        )
        return OperationResult.Success
    }

    override suspend fun getEntriesByQuery(query: DictQuery): List<DictWord> {
        return when (query.queryMode) {
            QueryMode.MATCH_ANYWHERE -> {
                dictDao.queryWithMatchAnyWhere(query.queryString).map {
                    DictWord(
                        it.id,
                        query.dictionary.languageFrom,
                        baseForm = it.baseForm,
                        alternativeForm = it.alternativeBaseForm
                    )
                }
            }
        }
    }

    override suspend fun getDictionaryById(dictionaryId: Int): DataOperationResult<Dictionary> {
        return DataOperationResult.Success(
            Dictionary(
                1,
                DictLanguage("name", "code"),
                DictLanguage("name", "code"),
                description = "description"
            )
        )
    }
//    externalDbHandler.checkDatabaseStructure(localDbFile).run {
//        if (this == DatabaseValidity.INVALID) {
//            return OperationResult.Failure
//        }
//
//    }

    override suspend fun importTableFromDb(importJob: ImportJob): OperationResult {
        return when (
            val readResult = externalDbHandler.readTableEntriesFrom(importJob.toImportEntry())
            ) {
            is DataOperationResult.Success -> {
                if (readResult.data.isNotEmpty()) {
                    dictDao.insertDictionary(
                        NewDictionary(
                            name = importJob.table.name,
                            languageFrom = importJob.table.languageFrom,
                            languageTo = importJob.table.languageTo
                        )
                    )
                    dictDao.insertDictEntries(readResult.data)
                }
                OperationResult.Success
            }
            is DataOperationResult.Failure -> {
                OperationResult.Failure(
                    errorMessage = "an error has occurred while reading table in importJob: $importJob",
                    cause = readResult
                )
            }
        }
    }

    override suspend fun readMetaInfoFromExternalDb(externalDatabase: ExternalDatabase): DataOperationResult<List<ExternalDatabaseTable>> {
        return when (
            val tableInfoFetch = externalDbHandler.readTableInfosFrom(
                externalDatabase.toExternalDictionaryResource()
            )
            ) {
            is DataOperationResult.Success -> {
                DataOperationResult.Success(
                    tableInfoFetch.data.map {
                        it.toExternalDatabaseTable()
                    }
                )
            }
            is DataOperationResult.Failure -> {
                DataOperationResult.Failure(
                    errorMessage = "an error has occurred while reading ${externalDatabase.uri}",
                    cause = tableInfoFetch
                )
            }
        }
    }

    override suspend fun readRegisteredDictionaries(): List<Dictionary> {
        return dictDao.getDictionaries().map {
            it.toDictionary()
        }
    }

}

private fun DictionaryLogEntryData.toDictionary(): Dictionary {
    return Dictionary(
        id = id,
        languageFrom = DictLanguage(
            name = Locale(languageFrom).getDisplayLanguage(Locale(languageFrom)),
            code = languageFrom
        ),
        languageTo = DictLanguage(
            name = Locale(languageTo).getDisplayLanguage(Locale(languageTo)),
            code = languageTo
        ),
        description = name
    )
}

private fun ImportJob.toImportEntry(): ImportEntry {
    return object : ImportEntry {
        override fun externalDictionaryResource(): ExternalDictionaryResource =
            resource.toExternalDictionaryResource()

        override fun tableInfo(): ImportEntry.TableInfo = table.toTableInfo()

    }
}

private fun ExternalDatabaseTable.toTableInfo(): ImportEntry.TableInfo {
    return object : ImportEntry.TableInfo {
        override fun name(): String = name
        override fun languageFrom(): String = languageFrom
        override fun languageTo(): String = languageTo
    }
}

private fun ExternalDatabase.toExternalDictionaryResource(): ExternalDictionaryResource {
    return object : ExternalDictionaryResource {
        override fun sdCardPath(): String = uri
    }
}

//private fun DictEntry.toDictTranslation(dictionary: Dictionary): DictTranslation {
//    return DictTranslation(
//        id = getId(),
//        dictionary = dictionary,
//        word =,
//        translation = getTranslation()
//    )
//}

private fun DictTranslation.toDictEntry(): DictEntry {
    return NewDictEntry(
        id = id,
        baseForm = word.baseForm,
        alternativeBaseForm = word.alternativeForm,
        translation = translation
    )
}

data class NewDictEntry(
    override val id: Int,
    override val baseForm: String,
    override val alternativeBaseForm: String,
    override val translation: String
) : DictEntry


data class NewDictionary(
    override val name: String,
    override val languageFrom: String,
    override val languageTo: String
) : NewDictionaryLogEntryData
