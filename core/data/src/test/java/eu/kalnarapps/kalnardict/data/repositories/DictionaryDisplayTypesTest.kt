package eu.kalnarapps.kalnardict.data.repositories

import eu.kalnarapps.kalnardict.data.Stubs
import eu.kalnarapps.kalnardict.data.datasources.dictionary.DictionaryDisplayTypeDataSource
import eu.kalnarapps.kalnardict.data.mapper.DataToDomainMapper
import eu.kalnarapps.kalnardict.data.model.contracts.DictionaryDisplayTypeDataEntry
import eu.kalnarapps.kalnardict.data.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.data.test.test
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class DictionaryDisplayTypesTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val dictionaryDisplayTypeDataSource =
        mock(DictionaryDisplayTypeDataSource::class.java)
    private val displayTypeMapper =
        mock(DataToDomainMapper::class.java) as DataToDomainMapper<DictionaryDisplayTypeDataEntry, DictionaryDisplayType>

    @Test
    fun return_display_type_for_dictionary() {

        testCoroutineRule.runBlockingTest {

            val dictionary = Stubs.Dictionaries.frenchToFrenchDictionary
            val expectedDisplayType = Stubs.Dictionaries.frenchToFrenchDictionaryDisplayType
            val expectedDisplayTypeDataEntry =
                Stubs.Dictionaries.frenchToFrenchDictionaryDisplayTypeDataEntry

            `when`(
                dictionaryDisplayTypeDataSource.displayTypeForDictionaryById(dictionary.id)
            ).thenReturn(
                flowOf(expectedDisplayTypeDataEntry)
            )
            `when`(
                displayTypeMapper.toDomainModel(expectedDisplayTypeDataEntry)
            ).thenReturn(
                expectedDisplayType
            )

            // when getting display type for dictionary
            val repository = DictionaryDisplayTypes(
                dictionaryDisplayTypeDataSource = dictionaryDisplayTypeDataSource,
                displayTypeMapper = displayTypeMapper
            )
            val displayType = repository.getDisplayTypeFor(dictionary)

            val testCollector = displayType.test(scope = this)
            try {
                testCollector.assertThatLastValue(
                    equalTo(expectedDisplayType)
                )
            } finally {
                testCollector.finish()
            }
        }


    }


}