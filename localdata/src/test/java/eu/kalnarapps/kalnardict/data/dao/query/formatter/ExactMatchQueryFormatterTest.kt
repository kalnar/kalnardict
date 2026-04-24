package eu.kalnarapps.kalnardict.data.dao.query.formatter

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ExactMatchQueryFormatterTest {

    @Test
    fun when_using_non_empty_string_inserts_percent_sign_around_each_letter() {

        val formatter = ExactMatchQueryFormatter()

        val word1 = "sampl"
        val formattedWord1 = """sampl"""

        assertThat(
            formatter.format(word1),
            equalTo(formattedWord1)
        )

        val word2 = "12%312%=_1_"
        val formattedWord2 = """12\%312\%=\_1\_"""

        assertThat(
            formatter.format(word2),
            equalTo(formattedWord2)
        )

    }

    @Test
    fun when_using_empty_string_return_single_percent_sign() {

        val formatter = MatchEndQueryFormatter()

        val empty = ""
        val formattedEmpty = """%"""

        assertThat(
            formatter.format(empty),
            equalTo(formattedEmpty)
        )

    }
}