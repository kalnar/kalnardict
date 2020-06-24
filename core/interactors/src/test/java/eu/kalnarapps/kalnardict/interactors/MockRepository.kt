package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.data.LanguageRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictTranslation
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord


class StubDictionaryRepository : DictionaryRepository {
    private val dictionaries = ArrayList<Dictionary>()
    override suspend fun insertDictEntry(dictTranslation: DictTranslation): OperationResult {
        TODO("Not yet implemented")
    }

    override suspend fun getEntriesByQuery(query: DictQuery): List<DictWord> {
        TODO("Not yet implemented")
    }

    override suspend fun importTableFromDb(importJob: ImportJob): OperationResult {
        val languageFrom = Stubs.Languages.all.find { importJob.table.languageFrom == it.code }
        val languageTo = Stubs.Languages.all.find { importJob.table.languageTo == it.code }
        return if (languageFrom != null && languageTo != null) {
            dictionaries.add(
                Dictionary(
                    id = dictionaries.size + 1,
                    languageFrom = languageFrom,
                    languageTo = languageTo,
                    description = importJob.displayName
                )
            )
            OperationResult.Success
        } else {
            OperationResult.Failure(
                errorMessage = "invalid language specified"
            )
        }
    }

    override suspend fun readMetaInfoFromExternalDb(externalDatabase: ExternalDatabase): DataOperationResult<List<ExternalDatabaseTable>> {
        return when (externalDatabase.uri) {
            Stubs.Uris.valid -> DataOperationResult.Success(
                listOf(
                    Stubs.MetaInfoOnDb.table1,
                    Stubs.MetaInfoOnDb.table2
                )
            )
            else -> DataOperationResult.Failure(
                errorMessage = "${externalDatabase.uri} does not contain valid tables"
            )
        }
    }

    override suspend fun readRegisteredDictionaries(): List<Dictionary> {
        return dictionaries
    }

    override suspend fun getDictionaryById(dictionaryId: Int): DataOperationResult<Dictionary> {
        TODO("Not yet implemented")
    }

}

class StubLanguageRepository : LanguageRepository {
    private val languages = ArrayList<DictLanguage>(
        listOf(
            Stubs.Languages.english,
            Stubs.Languages.french
        )
    )

    override suspend fun getLanguageById(id: String): DataOperationResult<DictLanguage> {
        return languages.find { it.code == id }?.let {
            DataOperationResult.Success(it)
        } ?: DataOperationResult.Failure(
            errorMessage = "no language with id: $id"
        )
    }

}