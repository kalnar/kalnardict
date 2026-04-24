package eu.kalnarapps.kalnardict.data.repositories

import eu.kalnarapps.kalnardict.data.Stubs
import eu.kalnarapps.kalnardict.data.datasources.dictionary.DictionaryDisplayTypeDataSource
import eu.kalnarapps.kalnardict.data.mapper.DataToDomainMapper
import eu.kalnarapps.kalnardict.data.mapper.DomainToDataMapper
import eu.kalnarapps.kalnardict.data.model.contracts.DictionaryDisplayTypeDataEntry
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock


class DictionaryDisplayTypesTest {

    private val dictionaryDisplayTypeDataSource =
        mock(DictionaryDisplayTypeDataSource::class.java)
    private val displayTypeMapper =
        mock(DataToDomainMapper::class.java) as DataToDomainMapper<DictionaryDisplayTypeDataEntry, DictionaryDisplayType>
    private val displayTypeDomainMapper =
        mock(DomainToDataMapper::class.java) as DomainToDataMapper<DictionaryDisplayType, DictionaryDisplayTypeDataEntry>

    @Test
    fun return_display_type_for_dictionary() {
        runTest {

            val dictionary = Stubs.Dictionaries.frenchToFrenchDictionary
            val expectedDisplayType = Stubs.Dictionaries.frenchToFrenchDictionaryDisplayType
            val expectedDisplayTypeDataEntry =
                Stubs.Dictionaries.frenchToFrenchDictionaryDisplayTypeDataEntry

            `when`(
                dictionaryDisplayTypeDataSource.displayTypeForDictionaryById(dictionary.id)
            ).thenReturn(
                expectedDisplayTypeDataEntry
            )
            `when`(
                displayTypeMapper.toDomainModel(expectedDisplayTypeDataEntry)
            ).thenReturn(
                expectedDisplayType
            )

            // when getting display type for dictionary
            val repository = DictionaryDisplayTypes(
                dictionaryDisplayTypeDataSource = dictionaryDisplayTypeDataSource,
                displayTypeDataMapper = displayTypeMapper,
                displayTypeDomainMapper = displayTypeDomainMapper
            )
            val displayType = repository.getDisplayTypeFor(dictionary)

            assertThat(displayType, equalTo(expectedDisplayType))
        }
    }

    @Test
    fun return_supported_display_types_for_dictionary() {
        runTest {

            val dictionary = Stubs.Dictionaries.frenchToFrenchDictionary
            val expectedSupportedDisplayType =
                Stubs.Dictionaries.frenchToFrenchDictionaryDisplayType
            val expectedSupportedDisplayTypes = listOf(
                expectedSupportedDisplayType
            )
            val expectedDisplayTypeDataEntry =
                Stubs.Dictionaries.frenchToFrenchDictionaryDisplayTypeDataEntry
            val expectedDisplayTypeDataEntries = listOf(
                expectedDisplayTypeDataEntry
            )

            `when`(
                dictionaryDisplayTypeDataSource.supportedDisplayTypesForDictionaryById(dictionary.id)
            ).thenReturn(
                flowOf(expectedDisplayTypeDataEntries)
            )
            `when`(
                displayTypeMapper.toDomainModel(expectedDisplayTypeDataEntry)
            ).thenReturn(
                expectedSupportedDisplayType
            )

            // when getting display type for dictionary
            val repository = DictionaryDisplayTypes(
                dictionaryDisplayTypeDataSource = dictionaryDisplayTypeDataSource,
                displayTypeDataMapper = displayTypeMapper,
                displayTypeDomainMapper = displayTypeDomainMapper
            )
            val displayType = repository.getSupportedDisplayTypesFor(dictionary).toList()

            assertThat(
                displayType.last(),
                equalTo(expectedSupportedDisplayTypes)
            )
        }
    }
}