package eu.kalnarapps.kalnardict.androidui.dictionarymanager

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.RegisterNewDictionaryUseCase
import java.util.Locale


class RegisterNewDictionaryMock(
    private val dictionaryListMock: DictionaryListMock
) : RegisterNewDictionaryUseCase {
    override suspend fun invoke(
        dbUri: String,
        originalName: String,
        savingName: String,
        languageFrom: String,
        languageTo: String
    ) {
        dictionaryListMock.add(
            Dictionary(
                id = dictionaryListMock.size + 1,
                languageFrom = DictLanguage(
                    name = Locale(languageFrom).displayName,
                    code = languageFrom
                ),
                languageTo = DictLanguage(
                    name = Locale(languageTo).displayName,
                    code = languageTo
                ),
                description = savingName
            )
        )
    }
}

class DictionaryListMock : ArrayList<Dictionary>()

class ListDictionariesMock(
    private val dictionaryListMock: DictionaryListMock
) : ListRegisteredDictionariesUseCase {
    override suspend fun invoke(): List<Dictionary> {
        return dictionaryListMock
    }
}