package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.data.LanguageRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictTranslation
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
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
        dictionaries.add(
            Dictionary(
                id = dictionaries.size + 1,
                languageFrom = importJob.table.languageFrom,
                languageTo = importJob.table.languageTo,
                description = importJob.displayName
            )
        )
        return OperationResult.Success
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