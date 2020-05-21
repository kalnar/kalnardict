package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
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

    override suspend fun insertDictEntry(dictTranslation: DictTranslation) {
        dictDao.insertDictEntry(
            dictTranslation.toDictEntry()
        )
    }

    override suspend fun getEntriesByQuery(query: DictQuery): List<DictWord> {
        return dictDao.getDictEntryByQuery(query.queryString).map {
            DictWord(
                it.getId(),
                DictLanguage("name", "code"),
                baseForm = it.getBaseForm(),
                alternativeForm = it.getAlternativeBaseForm()
            )
//            it.toDictTranslation(
//                dictionary = getDictionaryById(it.getDictionaryId())
//            )
        }
    }

    private fun getDictionaryById(dictionaryId: Int): Dictionary {
        return Dictionary(
            1,
            DictLanguage("name", "code"),
            DictLanguage("name", "code"),
            description = "descriptio"
        )
    }
//    externalDbHandler.checkDatabaseStructure(localDbFile).run {
//        if (this == DatabaseValidity.INVALID) {
//            return OperationResult.Failure
//        }
//
//    }

    override suspend fun importTablesFromDb(importJob: ImportJob): OperationResult {
        val readResult = externalDbHandler.readTableEntriesFrom(importJob.toImportEntry())
        return if (readResult is DataOperationResult.Success) {
            dictDao.insertDictEntries(readResult.data)
            OperationResult.Success
        } else {
            check(readResult is DataOperationResult.Failure) {
                "this is a bug, readResult should be a failure at this point"
            }
            OperationResult.Failure(
                errorMessage = "an error has occurred while reading table in importJob: $importJob",
                cause = readResult
            )
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

        override fun tableInfos(): List<ImportEntry.TableInfo> = tables.map { it.toTableInfo() }

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
    return object : DictEntry {
        override fun getId(): Int = id
        override fun getBaseForm(): String = word.baseForm
        override fun getAlternativeBaseForm(): String = word.alternativeForm
        override fun getTranslation(): String = translation
        override fun getDictionaryId(): Int = dictionary.id
    }
}

