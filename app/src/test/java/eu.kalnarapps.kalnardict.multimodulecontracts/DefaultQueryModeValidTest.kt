package eu.kalnarapps.kalnardict.multimodulecontracts

import eu.kalnarapps.kalnardict.data.entities.DataBaseConstants
import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsIterableContaining
import org.junit.Test

class DefaultQueryModeValidTest {

    @Test
    fun default_query_mode_id_is_valid_for_database() {
        assertThat(
            QueryMode.values().map { it.value },
            IsIterableContaining(
                equalTo(DataBaseConstants.DEFAULT_QUERY_MODE_ID.toIntOrNull())
            )
        )
    }
}