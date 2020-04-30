package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictTranslation
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.entities.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.entities.operations.OperationResult.Success
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord

class Repository(
    private val dictDao: DictDao,
    private val externalDbHandler: ExternalDatabaseHandler
) : DictionaryRepository {

    override suspend fun insertDictEntry(dictTranslation: DictTranslation) {
        dictDao.insertDictEntry(
            dictTranslation.toDictEntry()
        )
    }

    override suspend fun getEntriesByQuery(query: DictQuery): List<DictTranslation> {
        return dictDao.getDictEntryByQuery(query.queryString).map {
            it.toDictTranslation(
                dictionary = getDictionaryById(it.getDictionaryId())
            )
        }
    }

    private fun getDictionaryById(dictionaryId: Int): Dictionary {
        return Dictionary(
            1,
            DictLanguage("1", "name", "code"),
            DictLanguage("2", "name", "code"),
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
        val readResult = externalDbHandler.readTableFrom(importJob)
        return if (readResult is DataOperationResult.Success) {
            dictDao.insertDictEntries(readResult.data)
            Success
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

}

private fun DictEntry.toDictTranslation(dictionary: Dictionary): DictTranslation {
    return DictTranslation(
        id = getId(),
        dictionary = dictionary,
        word = DictWord(
            getId(),
            DictLanguage("1", "name", "code"),
            baseForm = getBaseForm(),
            alternativeForm = getAlternativeBaseForm()
        ),
        translation = getTranslation()
    )
}

private fun DictTranslation.toDictEntry(): DictEntry {
    return object : DictEntry {
        override fun getId(): Int = id
        override fun getBaseForm(): String = word.baseForm
        override fun getAlternativeBaseForm(): String = word.alternativeForm
        override fun getTranslation(): String = translation
        override fun getDictionaryId(): Int = dictionary.id
    }
}

