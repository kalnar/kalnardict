package eu.kalnarapps.kalnardict.presentation.mappers

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType
import eu.kalnarapps.kalnardict.presentation.interactors.PresentationStubs
import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class DictionaryMapperTest {

    private val displayTypeMapper =
        mock(DomainToUiMapper::class.java) as DomainToUiMapper<DictionaryDisplayType, RenderingStrategy>

    @Test
    fun when_mapping_a_dictionary_then_return_a_dictionary_ui_model() {

        `when`(
            displayTypeMapper.toUiModel(PresentationStubs.Dictionaries.DisplayTypes.frenchHtml)
        ).thenReturn(
            PresentationStubs.Dictionaries.RenderingStrategies.frenchHtml
        )

        val mapper = DictionaryMapper(
            displayTypeMapper = displayTypeMapper
        )

        // when mapping french dictionary
        val frenchDictionaryUiModel =
            mapper.toUiModel(PresentationStubs.Dictionaries.frenchWithDisplayTypeInfo)

        // then mapping result is dictionary ui model
        assertThat(
            frenchDictionaryUiModel,
            equalTo(PresentationStubs.Dictionaries.frenchUiModel)
        )

    }


}

