package eu.kalnarapps.kalnardict.androidui.dependencies.mocks

import eu.kalnarapps.kalnardict.androidui.stub.Stubs
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictTranslation
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

    override suspend fun insertDictEntry(dictTranslation: DictTranslation): OperationResult {
        return if (dictTranslation.dictionary in dictDao.keys) {
            val translations = dictDao[dictTranslation.dictionary]
            check(translations != null)
            dictDao[dictTranslation.dictionary] = translations.plus(dictTranslation)
            OperationResult.Success
        } else {
            OperationResult.Failure(
                errorMessage = "dictionary tables are not set up for dictionary: ${dictTranslation.dictionary}"
            )
        }
    }

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
        return Stubs.Domain.Dictionaries.ALL.find { it.id == dictionaryId }?.let {
            DataOperationResult.Success(it)
        } ?: DataOperationResult.Failure(
            errorMessage = "no dictionaries found with given id: $dictionaryId"
        )
    }
}
