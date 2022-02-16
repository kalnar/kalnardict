package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.interactors.mock.MockLanguageRepository
import eu.kalnarapps.kalnardict.interactors.test.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsIterableContainingInAnyOrder
import org.hamcrest.core.IsCollectionContaining
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test


class RegisterNewLanguageTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Test
    fun register_new_dictionary_with_unique_id() {
        testCoroutineRule.runBlockingTest {

            val mockRepository = MockLanguageRepository(ArrayList(Stubs.Languages.frenchAndEnglish))
            // given that registered languages are english and french
            val registerLanguageUseCase = RegisterNewLanguage(mockRepository)
            val listRegisteredLanguagesUseCase = ListAvailableLanguages(mockRepository)
            assertThat(
                listRegisteredLanguagesUseCase(),
                not(
                    IsCollectionContaining(
                        equalTo(Stubs.Languages.russian)
                    )
                )
            )

            // when I register russian
            val result = registerLanguageUseCase(
                Stubs.Languages.russian
            )

            // then the operation is a success and listing available languages gives en,fr and ru

            assertThat(
                result,
                IsInstanceOf(OperationResult.Success::class.java)
            )

            assertThat(
                listRegisteredLanguagesUseCase(),
                IsIterableContainingInAnyOrder(
                    listOf(
                        Stubs.Languages.russian,
                        Stubs.Languages.french,
                        Stubs.Languages.english
                    ).map {
                        equalTo(it)
                    }
                )
            )


        }
    }

    @Test
    fun attempt_to_register_new_dictionary_with_used_id_and_fail() {
        testCoroutineRule.runBlockingTest {

            val mockRepository = MockLanguageRepository(ArrayList(Stubs.Languages.frenchAndEnglish))
            // given that registered languages are english and french
            val registerLanguageUseCase = RegisterNewLanguage(mockRepository)
            val listRegisteredLanguagesUseCase = ListAvailableLanguages(mockRepository)
            assertThat(
                listRegisteredLanguagesUseCase(),
                IsCollectionContaining(
                    equalTo(Stubs.Languages.french)
                )
            )

            // when I register french
            val result = registerLanguageUseCase(
                Stubs.Languages.french
            )

            // then the operation is a failure
            assertThat(
                result,
                IsInstanceOf(OperationResult.Failure::class.java)
            )
        }
    }
}