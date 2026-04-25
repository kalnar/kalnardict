package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.data.DisplayTypeRepository
import eu.kalnarapps.kalnardict.data.LanguageRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportProgress
import eu.kalnarapps.kalnardict.interactors.RegisterNewDictionary.Companion.DB_BATCH_SIZE
import eu.kalnarapps.kalnardict.interactors.test.anyNonNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever


class RegisterNewDictionaryTestNew {

    private val dictionaryRepository = mock(DictionaryRepository::class.java)
    private val displayTypeRepository = mock(DisplayTypeRepository::class.java)
    private val stubLanguageRepository = mock(LanguageRepository::class.java)
    private val stubConfigurationRepository = mock(ConfigurationRepository::class.java)

    private suspend fun setUp() {
        doAnswer { invocationOnMock ->
            val language: DictLanguage? =
                Stubs.Languages.frenchAndEnglish.find { it.code == (invocationOnMock.arguments[0] as String) }
            if (language != null) {
                DataOperationResult.Success(language)
            } else {
                DataOperationResult.Failure<DictLanguage>("")
            }
        }.`when`(stubLanguageRepository).getLanguageById(ArgumentMatchers.anyString())
    }

    @Test
    fun when_register_new_dictionary_with_valid_table_call_import_table() {
        runTest {
            // given 3 languages and db table is valid

            setUp()

            val importJob = ImportJob(
                    table = ExternalDatabaseTable(
                        name = Stubs.RegisteringDictionary.originalTableName,
                        languageFrom = Stubs.RegisteringDictionary.sourceLangCode,
                        languageTo = Stubs.RegisteringDictionary.destinationLangCode
                    ),
                    resource = ExternalDatabase.LocalFile(Stubs.RegisteringDictionary.dbUriStub),
                    displayName = Stubs.RegisteringDictionary.dictionaryName,
                    batchSize = DB_BATCH_SIZE
                )

            whenever(
                dictionaryRepository.importTableFromDb(importJob)
            ).thenReturn(
                flowOf(
                    DataOperationResult.Success(
                        ImportProgress(
                            Stubs.Dictionaries.englishToFrenchDictionaryId,
                            10,
                            10
                        )
                    )
                )
            )

            whenever(
                dictionaryRepository.getDictionaryById(
                    Stubs.Dictionaries.englishToFrenchDictionaryId
                )
            ).thenReturn(
                DataOperationResult.Success(
                    Stubs.Dictionaries.englishToFrenchDictionary
                )
            )

            val registerNewDictionary = RegisterNewDictionary(
                dictionaryRepository = dictionaryRepository,
                displayTypeRepository = displayTypeRepository,
                languageRepository = stubLanguageRepository,
                configurationRepository = stubConfigurationRepository,
            )

            // when calling registerNewDictionary
            val flow = registerNewDictionary(
                dbUri = Stubs.RegisteringDictionary.dbUriStub,
                originalName = Stubs.RegisteringDictionary.originalTableName,
                languageFrom = Stubs.RegisteringDictionary.sourceLangCode,
                languageTo = Stubs.RegisteringDictionary.destinationLangCode,
                savingName = Stubs.RegisteringDictionary.dictionaryName
            ).toList().first()

            // then we start importing
            verify(dictionaryRepository)
                .importTableFromDb(anyNonNull())

            verify(stubConfigurationRepository)
                .updateCurrentDictionary(Stubs.Dictionaries.englishToFrenchDictionary)

            verify(displayTypeRepository)
                .addDisplayTypesFor(
                    Stubs.Dictionaries.englishToFrenchDictionaryId,
                    importJob.displayTypes
                )
        }
    }

    @Test
    fun when_register_new_dictionary_with_invalid_language_emit_failure() {
        runTest {
            // given 3 languages and db table is valid

            setUp()

            val registerNewDictionary = RegisterNewDictionary(
                dictionaryRepository = dictionaryRepository,
                displayTypeRepository = displayTypeRepository,
                languageRepository = stubLanguageRepository,
                configurationRepository = stubConfigurationRepository,
            )

            // when calling registerNewDictionary then emit failure
            val result = registerNewDictionary(
                dbUri = Stubs.RegisteringDictionary.dbUriStub,
                originalName = Stubs.RegisteringDictionary.originalTableName,
                languageFrom = Stubs.Languages.russian.code,
                languageTo = Stubs.RegisteringDictionary.destinationLangCode,
                savingName = Stubs.RegisteringDictionary.dictionaryName
            ).toList().first()

            assertThat(result, IsInstanceOf(DataOperationResult.Failure::class.java))
        }
    }
}

