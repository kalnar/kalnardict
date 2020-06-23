package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.dao.DictDao
import eu.kalnarapps.kalnardict.data.mapper.DictEntry
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.NewDictionaryLogEntryData
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictTranslation
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord
import java.net.URI
import java.util.*

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
        return dictDao.getDictEntryByQuery(query.queryString).map {
            DictWord(
                it.id,
                DictLanguage("name", "code"),
                baseForm = it.baseForm,
                alternativeForm = it.alternativeBaseForm
            )
//            it.toDictTranslation(
//                dictionary = getDictionaryById(it.getDictionaryId())
//            )
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
                            languageFrom = importJob.table.languageFrom.name,
                            languageTo = importJob.table.languageTo.name
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
        override fun languageFrom(): String = languageFrom.code
        override fun languageTo(): String = languageTo.code
    }
}

private fun ExternalDatabase.toExternalDictionaryResource(): ExternalDictionaryResource {
    return object : ExternalDictionaryResource {
        override fun uri(): URI = uri
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
