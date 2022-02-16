package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.interactors.test.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.collection.IsIterableContainingInAnyOrder
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test


class ListMetaInfoOnDbTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    private val listMetaInfoOnDb = ListMetaInfoOnDb(StubDictionaryRepository())

    @Test
    fun attempt_to_read_invalid_file() {
        testCoroutineRule.runBlockingTest {
            val metaInfoFetch = listMetaInfoOnDb(Stubs.Uris.invalidUri)

            assertThat(
                metaInfoFetch,
                IsInstanceOf(DataOperationResult.Failure::class.java)
            )
        }
    }

    @Test
    fun read_meta_table_info() {
        testCoroutineRule.runBlockingTest {
            val metaInfoFetch = listMetaInfoOnDb(Stubs.Uris.valid)

            assertThat(
                metaInfoFetch,
                IsInstanceOf(DataOperationResult.Success::class.java)
            )
            check(metaInfoFetch is DataOperationResult.Success)

            assertThat(
                metaInfoFetch.data,
                IsIterableContainingInAnyOrder(
                    listOf(
                        equalTo(Stubs.MetaInfoOnDb.table1),
                        equalTo(Stubs.MetaInfoOnDb.table2)
                    )
                )
            )
        }
    }
}