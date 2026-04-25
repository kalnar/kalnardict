package eu.kalnarapps.kalnardict.interactors.displaytypes

import eu.kalnarapps.kalnardict.data.DisplayTypeRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayTypeInfo
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DisplayTypeInfo
import eu.kalnarapps.kalnardict.interactors.Stubs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`


class GetDictionaryWithDisplayTypeInfoTest {

    private val displayTypeRepository = mock(DisplayTypeRepository::class.java)

    @Test
    fun return_dictionary_with_display_info_as_expected() {
        runTest {

            val dictionary = Stubs.Dictionaries.frenchToFrenchDictionary
            val currentDisplayType = Stubs.Dictionaries.frenchToFrenchDictionaryDisplayType
            val supportedDisplayTypes =
                Stubs.Dictionaries.frenchToFrenchDictionarySupportedDisplayTypes
            `when`(displayTypeRepository.getDisplayTypeFlowFor(dictionary)).thenReturn(
                flowOf(currentDisplayType)
            )
            `when`(displayTypeRepository.getSupportedDisplayTypesFor(dictionary)).thenReturn(
                flowOf(supportedDisplayTypes)
            )

            val useCase = GetDictionaryWithDisplayTypeInfo(
                displayTypeRepository = displayTypeRepository
            )

            // when calling use case
            val frenchToFrenchDictionaryWithDisplayTypeInfo =
                useCase(dictionary).single()

            assertThat(
                frenchToFrenchDictionaryWithDisplayTypeInfo,
                equalTo(
                    DictionaryWithDisplayTypeInfo(
                        dictionary = dictionary,
                        displayTypeInfo = DisplayTypeInfo(
                            displayType = currentDisplayType,
                            supportedDisplayTypes = supportedDisplayTypes
                        )
                    )
                )
            )
        }
    }
}