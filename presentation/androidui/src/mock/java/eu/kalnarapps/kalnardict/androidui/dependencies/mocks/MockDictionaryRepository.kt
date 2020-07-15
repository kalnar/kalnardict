package eu.kalnarapps.kalnardict.androidui.dependencies.mocks

import eu.kalnarapps.kalnardict.androidui.stub.Stubs
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord

class MockDictionaryRepository : DictionaryRepository {
    private val dictDao = hashMapOf(
        Stubs.Domain.Dictionaries.englishDict to Stubs.Domain.Dictionaries.Translations.englishEnglishTranslations,
        Stubs.Domain.Dictionaries.englishFrenchDict to Stubs.Domain.Dictionaries.Translations.englishFrenchTranslations
    )

    override suspend fun getEntriesByQuery(query: DictQuery): List<DictWord> {
        return dictDao[query.dictionary].orEmpty().map { it.word }
            .filter {
                it.baseForm.contains(query.queryString)
            }
    }

    override suspend fun importTableFromDb(importJob: ImportJob): OperationResult {
        dictDao[Stubs.Domain.Dictionaries.frenchEnglishDict] =
            Stubs.Domain.Dictionaries.Translations.frenchEnglishTranslations
        return OperationResult.Success
    }

    override suspend fun readMetaInfoFromExternalDb(externalDatabase: ExternalDatabase): DataOperationResult<List<ExternalDatabaseTable>> {
        TODO("Not yet implemented")
    }

    override suspend fun readRegisteredDictionaries(): List<Dictionary> {
        return dictDao.keys.toList()
    }

    override suspend fun getDictionaryById(dictionaryId: Int): DataOperationResult<Dictionary> {
        return Stubs.Domain.Dictionaries.englishAndFrenchDicts.find { it.id == dictionaryId }?.let {
            DataOperationResult.Success(it)
        } ?: DataOperationResult.Failure(
            errorMessage = "no dictionaries found with given id: $dictionaryId"
        )
    }

    override suspend fun getTranslationById(
        wordId: Int,
        dictionaryId: Int
    ): DataOperationResult<String> {
        TODO("Not yet implemented")
    }
}
