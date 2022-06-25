package eu.kalnarapps.kalnardict.presentation.interactors.dictionary

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayTypeInfo
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DisplayTypeInfo
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.displaytypes.GetDictionaryWithDisplayTypeInfoUseCase
import eu.kalnarapps.kalnardict.presentation.interactors.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.presentation.interactors.test.test
import eu.kalnarapps.kalnardict.presentation.mappers.DomainToUiMapper
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.DictionaryUpdateUi
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.ManageableDictionaryView
import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ListManageableDictionariesForUiTest {

    private val listRegisteredDictionaries: ListRegisteredDictionariesUseCase = mock()
    private val getDictionaryWithDisplayTypeInfoUseCase: GetDictionaryWithDisplayTypeInfoUseCase =
        mock()
    private val dictionaryMapper: DomainToUiMapper<DictionaryWithDisplayTypeInfo, ManageableDictionaryView> =
        mock()

    private val listManageableDictionariesUseCaseForUi = ListManageableDictionariesForUi(
        listRegisteredDictionaries, getDictionaryWithDisplayTypeInfoUseCase, dictionaryMapper
    )

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun when_deleting_dictionary_then_update_flow() {

        testCoroutineRule.runBlockingTest {

            val firstDictionaryDomain = Dictionary(
                1,
                DictLanguage("from", "from"),
                DictLanguage("to", "to"),
                "from <from> to <to>"
            )

            val secondDictionaryDomain = Dictionary(
                2,
                DictLanguage("from", "from"),
                DictLanguage("to", "to"),
                "from <from> to <to>"
            )

            val firstDictionaryDisplayType = DisplayTypeInfo(
                displayType = DictionaryDisplayType.HTML,
                supportedDisplayTypes = listOf(
                    DictionaryDisplayType.HTML,
                    DictionaryDisplayType.TEXT
                )
            )

            val secondDictionaryDisplayType = DisplayTypeInfo(
                displayType = DictionaryDisplayType.TEXT,
                supportedDisplayTypes = listOf(
                    DictionaryDisplayType.HTML,
                    DictionaryDisplayType.TEXT
                )
            )

            val firstDictionaryWithDisplayTypeInfo = DictionaryWithDisplayTypeInfo(
                firstDictionaryDomain,
                firstDictionaryDisplayType
            )

            val secondDictionaryWithDisplayTypeInfo = DictionaryWithDisplayTypeInfo(
                secondDictionaryDomain,
                secondDictionaryDisplayType
            )

            val firstDictionary = ManageableDictionaryView(
                dictionaryName = "name1",
                sourceLanguage = "from",
                destinationLanguage = "to",
                currentRenderingStrategy = RenderingStrategy("html", "html"),
                availableRenderingStrategy = listOf(RenderingStrategy("html", "html")),
                updateInfo = DictionaryUpdateUi.None,
                dictionaryId = 1
            )

            val secondDictionary = ManageableDictionaryView(
                dictionaryName = "name2",
                sourceLanguage = "from",
                destinationLanguage = "to",
                currentRenderingStrategy = RenderingStrategy("html", "html"),
                availableRenderingStrategy = listOf(RenderingStrategy("html", "html")),
                updateInfo = DictionaryUpdateUi.None,
                dictionaryId = 2
            )

            val registeredDictionaries = MutableStateFlow(
                listOf(firstDictionaryDomain, secondDictionaryDomain)
            )

            whenever(listRegisteredDictionaries.invoke()).thenReturn(registeredDictionaries)

            whenever(
                dictionaryMapper.toUiModel(
                    DictionaryWithDisplayTypeInfo(
                        dictionary = firstDictionaryDomain,
                        displayTypeInfo = firstDictionaryDisplayType
                    )
                )
            ).thenReturn(
                firstDictionary
            )

            whenever(
                getDictionaryWithDisplayTypeInfoUseCase.invoke(firstDictionaryDomain)
            ).thenReturn(
                flowOf(firstDictionaryWithDisplayTypeInfo)
            )
            whenever(
                getDictionaryWithDisplayTypeInfoUseCase.invoke(secondDictionaryDomain)
            ).thenReturn(
                flowOf(secondDictionaryWithDisplayTypeInfo)
            )

            whenever(
                dictionaryMapper.toUiModel(
                    DictionaryWithDisplayTypeInfo(
                        dictionary = secondDictionaryDomain,
                        displayTypeInfo = secondDictionaryDisplayType
                    )
                )
            ).thenReturn(
                secondDictionary
            )

            val observer = listManageableDictionariesUseCaseForUi.invoke().test(this)

            verify(listRegisteredDictionaries).invoke()
            verify(getDictionaryWithDisplayTypeInfoUseCase).invoke(firstDictionaryDomain)
            verify(dictionaryMapper).toUiModel(
                DictionaryWithDisplayTypeInfo(
                    firstDictionaryDomain,
                    firstDictionaryDisplayType
                )
            )
            verify(dictionaryMapper).toUiModel(
                DictionaryWithDisplayTypeInfo(
                    secondDictionaryDomain,
                    secondDictionaryDisplayType
                )
            )

            observer.assertThat(
                { it.last() },
                equalTo(
                    listOf(firstDictionary, secondDictionary)
                )
            )

            registeredDictionaries.value = listOf(firstDictionaryDomain)

            observer.assertThat(
                { it.last() },
                equalTo(
                    listOf(firstDictionary)
                )
            )

            observer.finish()
        }


    }

    @Test
    fun when_deleting_last_dictionary_then_update_flow_with_empty_list() {

        testCoroutineRule.runBlockingTest {

            val firstDictionaryDomain = Dictionary(
                1,
                DictLanguage("from", "from"),
                DictLanguage("to", "to"),
                "from <from> to <to>"
            )

            val firstDictionaryDisplayType = DisplayTypeInfo(
                displayType = DictionaryDisplayType.HTML,
                supportedDisplayTypes = listOf(
                    DictionaryDisplayType.HTML,
                    DictionaryDisplayType.TEXT
                )
            )

            val firstDictionaryWithDisplayTypeInfo = DictionaryWithDisplayTypeInfo(
                firstDictionaryDomain,
                firstDictionaryDisplayType
            )

            val firstDictionary = ManageableDictionaryView(
                dictionaryName = "name1",
                sourceLanguage = "from",
                destinationLanguage = "to",
                currentRenderingStrategy = RenderingStrategy("html", "html"),
                availableRenderingStrategy = listOf(RenderingStrategy("html", "html")),
                updateInfo = DictionaryUpdateUi.None,
                dictionaryId = 1
            )

            val registeredDictionaries = MutableStateFlow(
                listOf(firstDictionaryDomain)
            )

            whenever(listRegisteredDictionaries.invoke()).thenReturn(registeredDictionaries)

            whenever(
                dictionaryMapper.toUiModel(
                    DictionaryWithDisplayTypeInfo(
                        dictionary = firstDictionaryDomain,
                        displayTypeInfo = firstDictionaryDisplayType
                    )
                )
            ).thenReturn(
                firstDictionary
            )

            whenever(
                getDictionaryWithDisplayTypeInfoUseCase.invoke(firstDictionaryDomain)
            ).thenReturn(
                flowOf(firstDictionaryWithDisplayTypeInfo)
            )

            val observer = listManageableDictionariesUseCaseForUi.invoke().test(this)

            verify(listRegisteredDictionaries).invoke()
            verify(getDictionaryWithDisplayTypeInfoUseCase).invoke(firstDictionaryDomain)
            verify(dictionaryMapper).toUiModel(
                DictionaryWithDisplayTypeInfo(
                    firstDictionaryDomain,
                    firstDictionaryDisplayType
                )
            )

            observer.assertThat(
                { it.last() },
                equalTo(
                    listOf(firstDictionary)
                )
            )

            registeredDictionaries.value = emptyList()

            observer.assertThat(
                { it.last() },
                equalTo(
                    emptyList()
                )
            )

            observer.finish()
        }


    }

    @Test
    fun when_starting_with_empty_list_then_emit_empty_list() {

        testCoroutineRule.runBlockingTest {

            val registeredDictionaries = MutableStateFlow<List<Dictionary>>(
                emptyList()
            )

            val firstDictionaryDomain = Dictionary(
                1,
                DictLanguage("from", "from"),
                DictLanguage("to", "to"),
                "from <from> to <to>"
            )

            val firstDictionaryDisplayType = DisplayTypeInfo(
                displayType = DictionaryDisplayType.HTML,
                supportedDisplayTypes = listOf(
                    DictionaryDisplayType.HTML,
                    DictionaryDisplayType.TEXT
                )
            )

            val firstDictionaryWithDisplayTypeInfo = DictionaryWithDisplayTypeInfo(
                firstDictionaryDomain,
                firstDictionaryDisplayType
            )

            val firstDictionary = ManageableDictionaryView(
                dictionaryName = "name1",
                sourceLanguage = "from",
                destinationLanguage = "to",
                currentRenderingStrategy = RenderingStrategy("html", "html"),
                availableRenderingStrategy = listOf(RenderingStrategy("html", "html")),
                updateInfo = DictionaryUpdateUi.None,
                dictionaryId = 1
            )

            whenever(listRegisteredDictionaries.invoke()).thenReturn(registeredDictionaries)

            whenever(
                dictionaryMapper.toUiModel(
                    DictionaryWithDisplayTypeInfo(
                        dictionary = firstDictionaryDomain,
                        displayTypeInfo = firstDictionaryDisplayType
                    )
                )
            ).thenReturn(
                firstDictionary
            )

            whenever(
                getDictionaryWithDisplayTypeInfoUseCase.invoke(firstDictionaryDomain)
            ).thenReturn(
                flowOf(firstDictionaryWithDisplayTypeInfo)
            )

            val observer = listManageableDictionariesUseCaseForUi.invoke().test(this)

            verify(listRegisteredDictionaries).invoke()

            observer.assertThat(
                { it.last() },
                equalTo(
                    emptyList()
                )
            )

            registeredDictionaries.value = listOf(firstDictionaryDomain)

            observer.assertThat(
                { it.last() },
                equalTo(
                    listOf(firstDictionary)
                )
            )

            observer.finish()
        }


    }

}