package eu.kalnarapps.kalnardict.presentation.interactors

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayType
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.displaytypes.GetDictionaryWithDisplayTypeInfoUseCase
import eu.kalnarapps.kalnardict.domain.usecases.displaytypes.GetDictionaryWithDisplayTypeUseCase
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.ListRegisteredDictionariesForUi
import eu.kalnarapps.kalnardict.presentation.interactors.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.presentation.interactors.test.test
import eu.kalnarapps.kalnardict.presentation.mappers.DomainToUiMapper
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionaryUiModel
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.IsNot.not
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock


class ListRegisteredDictionariesForUiTest {

    private val listRegisteredDictionaries = mock(
        ListRegisteredDictionariesUseCase::class.java
    )
    private val getDictionaryWithDisplayTypeInfoUseCase = mock(
        GetDictionaryWithDisplayTypeInfoUseCase::class.java
    )
    private val dictionaryMapper =
        mock(DomainToUiMapper::class.java) as DomainToUiMapper<DictionaryWithDisplayType, DictionaryUiModel>

    private val getDictionaryWithDisplayType =
        mock(GetDictionaryWithDisplayTypeUseCase::class.java)

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()


    @Test
    fun when_listing_dictionaries_for_ui_then_list_them() {

        testCoroutineRule.runBlockingTest {

            // given there is one dictionary
            `when`(listRegisteredDictionaries()).thenReturn(
                flowOf(listOf(PresentationStubs.Dictionaries.french))
            )

            `when`(
                getDictionaryWithDisplayTypeInfoUseCase(
                    PresentationStubs.Dictionaries.french
                )
            ).thenReturn(
                flowOf(PresentationStubs.Dictionaries.frenchWithDisplayTypeInfo)
            )
            `when`(
                getDictionaryWithDisplayType(PresentationStubs.Dictionaries.french)
            ).thenReturn(
                PresentationStubs.Dictionaries.frenchWithDisplayType
            )
            `when`(
                dictionaryMapper.toUiModel(PresentationStubs.Dictionaries.frenchWithDisplayType)
            ).thenReturn(
                PresentationStubs.Dictionaries.frenchUiModel
            )


            val useCase = ListRegisteredDictionariesForUi(
                listRegisteredDictionaries = listRegisteredDictionaries,
                getDictionaryWithDisplayTypeInfoUseCase = getDictionaryWithDisplayTypeInfoUseCase,
                dictionaryMapper = dictionaryMapper,
                getDictionaryWithDisplayType = getDictionaryWithDisplayType
            )

            val testCollector = useCase().test(scope = this)

            try {
                testCollector.assertThat(
                    { it.last() as Collection<DictionaryUiModel> },
                    not(IsEmptyCollection())
                )
                testCollector.assertThatLastValue(
                    equalTo(listOf(PresentationStubs.Dictionaries.frenchUiModel))
                )
            } finally {
                testCollector.finish()
            }

        }

    }

}