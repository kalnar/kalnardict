package eu.kalnarapps.kalnardict.interactors.displaytypes

import eu.kalnarapps.kalnardict.data.DisplayTypeRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayTypeInfo
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DisplayTypeInfo
import eu.kalnarapps.kalnardict.interactors.Stubs
import eu.kalnarapps.kalnardict.interactors.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.interactors.test.test
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock


class GetDictionaryWithDisplayTypeInfoTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val displayTypeRepository = mock(DisplayTypeRepository::class.java)

    @Test
    fun return_dictionary_with_display_info_as_expected() {
        testCoroutineRule.runBlockingTest {

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
                useCase(dictionary)

            // then result contains display type info
            val testCollector = frenchToFrenchDictionaryWithDisplayTypeInfo.test(scope = this)
            try {
                testCollector.assertThatLastValue(
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
            } finally {
                testCollector.finish()
            }

        }
    }


}