package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import eu.kalnarapps.kalnardict.androidui.UiUnitTestStubs
import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.ReadExternalDbUseCaseMock
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.DictionaryListMock
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.RegisterNewDictionaryMockWithFailures
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.RegisterNewDictionarySuccessfullyMock
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.mocks.ListLanguagesMock
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.mocks.MockRegisterLanguageUseCase
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.mapper.LanguageMapper
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.RegistryError
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.SelectableLanguage
import eu.kalnarapps.kalnardict.androidui.stub.Stubs
import eu.kalnarapps.kalnardict.androidui.stub.UiStubs
import eu.kalnarapps.kalnardict.androidui.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.androidui.test.TestDispatcherProvider
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredLanguagesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ReadExternalDbUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.*
import org.hamcrest.collection.IsCollectionWithSize
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.collection.IsIterableContainingInAnyOrder
import org.hamcrest.core.IsCollectionContaining
import org.hamcrest.core.IsInstanceOf
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@ExperimentalCoroutinesApi
class DictionaryRegistryViewModelTest : KoinComponent {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setUp() {
        startKoin {
            modules(
                listOf(
                    module {
                        single { ReadExternalDbUseCaseMock() as ReadExternalDbUseCase }
                        single {
                            DictionaryListMock().apply {
                                addAll(Stubs.Domain.Dictionaries.englishAndFrenchDicts)
                            }
                        }
                        single {
                            ListLanguagesMock(
                                ArrayList(Stubs.Domain.Languages.frenchAndEnglishLanguage)
                            ) as ListRegisteredLanguagesUseCase
                        }
                    }
                )
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun list_found_tables_in_ui() {

        val viewModel = DictionaryRegistryViewModel(
            dbPath = UiStubs.Uris.validUri,
            loadDbMetaInfoOnDb = getKoin().get(),
            registerNewDictionary = RegisterNewDictionarySuccessfullyMock(),
            dispatcherProvider = TestDispatcherProvider,
            listAvailableLanguages = getKoin().get(),
            languageMapper = LanguageMapper(),
            addNewLanguage = MockRegisterLanguageUseCase()
        )

        val listOfTableUi = viewModel.getRegisterDictionaryUiModels().map { it.tableUiInfo }

        assertThat(
            listOfTableUi,
            not(IsEmptyCollection())
        )
    }

    @Test
    fun load_view_model_with_erroneous_uri() {

        val viewModel = DictionaryRegistryViewModel(
            UiStubs.Uris.invalidUri,
            loadDbMetaInfoOnDb = getKoin().get(),
            registerNewDictionary = RegisterNewDictionarySuccessfullyMock(),
            dispatcherProvider = TestDispatcherProvider,
            listAvailableLanguages = getKoin().get(),
            languageMapper = LanguageMapper(),
            addNewLanguage = MockRegisterLanguageUseCase()
        )

        val listOfTableUi = viewModel.getRegisterDictionaryUiModels().map { it.tableUiInfo }

        assertThat(
            listOfTableUi,
            IsEmptyCollection()
        )
        assertThat<String>(
            viewModel.error.value?.logMessage,
            containsString("not of correct")
        )
    }


    @Test
    fun update_registering_table_information() {

        val viewModel = DictionaryRegistryViewModel(
            UiStubs.Uris.validUri,
            loadDbMetaInfoOnDb = getKoin().get(),
            registerNewDictionary = RegisterNewDictionarySuccessfullyMock(),
            dispatcherProvider = TestDispatcherProvider,
            listAvailableLanguages = getKoin().get(),
            languageMapper = LanguageMapper(),
            addNewLanguage = MockRegisterLanguageUseCase()
        )

        val listOfTableUi = viewModel.getRegisterDictionaryUiModels().map { it.tableUiInfo }

        assertThat(
            listOfTableUi,
            not(IsEmptyCollection())
        )

        val firstTable = listOfTableUi.first()
        val firstTableNewDictionaryName = "mock new name"


        assertThat(
            firstTable.dictionaryName,
            not(equalTo(firstTableNewDictionaryName))
        )

        viewModel.onTableRegisteringUpdate(
            firstTable.copy(dictionaryName = firstTableNewDictionaryName)
        )

        val listOfTableUiModels = viewModel.getRegisterDictionaryUiModels().map { it.tableUiInfo }
        val firstTableFromNewFetch = listOfTableUiModels.first()

        assertThat(
            firstTableFromNewFetch.dictionaryName,
            equalTo(firstTableNewDictionaryName)
        )

    }

    @Test
    fun register_all_dictionaries_with_success() {

        val viewModel = DictionaryRegistryViewModel(
            UiStubs.Uris.validUri,
            loadDbMetaInfoOnDb = getKoin().get(),
            registerNewDictionary = RegisterNewDictionarySuccessfullyMock(),
            dispatcherProvider = TestDispatcherProvider,
            listAvailableLanguages = getKoin().get(),
            languageMapper = LanguageMapper(),
            addNewLanguage = MockRegisterLanguageUseCase()
        )

        val tables = viewModel.getRegisterDictionaryUiModels().map { it.tableUiInfo }

        assertThat(
            viewModel.error.value,
            IsNull()
        )
        assertThat(
            tables,
            not(IsEmptyCollection())
        )

        viewModel.registerDictionaries()

        assertThat(
            viewModel.error.value,
            IsNull()
        )
    }

    @Test
    fun register_all_dictionaries_with_success_but_last() {

        val viewModel = DictionaryRegistryViewModel(
            UiStubs.Uris.validUri,
            loadDbMetaInfoOnDb = getKoin().get(),
            registerNewDictionary = RegisterNewDictionaryMockWithFailures(listOf(2)),
            dispatcherProvider = TestDispatcherProvider,
            listAvailableLanguages = getKoin().get(),
            languageMapper = LanguageMapper(),
            addNewLanguage = MockRegisterLanguageUseCase()
        )
        viewModel.onTableRegisteringUpdate(
            UiStubs.TableUiInfo.externalTable1
        )
        viewModel.onTableRegisteringUpdate(
            UiStubs.TableUiInfo.externalTable2
        )

        val tables = viewModel.getRegisterDictionaryUiModels().map { it.tableUiInfo }

        assertThat(
            viewModel.error.value,
            IsNull()
        )
        assertThat(
            tables?.filter { it.isSelected },
            IsCollectionWithSize(equalTo(2))
        )

        viewModel.registerDictionaries()

        assertThat(
            viewModel.error.value,
            not(IsNull())
        )

        val lastResult = viewModel.getRegistrationStatus().last().result
        assertThat(
            lastResult,
            IsInstanceOf(OperationResult.Failure::class.java)
        )

        check(lastResult is OperationResult.Failure)

        assertThat(
            lastResult,
            equalTo(OperationResult.Failure(UiUnitTestStubs.NEW_DICT_USE_CASE_ERROR_MSG))
        )
    }

    @Test
    fun get_available_languages_when_available() {

        val viewModel = DictionaryRegistryViewModel(
            UiStubs.Uris.validUri,
            loadDbMetaInfoOnDb = getKoin().get(),
            registerNewDictionary = RegisterNewDictionarySuccessfullyMock(),
            dispatcherProvider = TestDispatcherProvider,
            listAvailableLanguages = getKoin().get(),
            languageMapper = LanguageMapper(),
            addNewLanguage = MockRegisterLanguageUseCase()
        )

        assertThat(
            viewModel.getSelectableLanguages(),
            IsIterableContainingInAnyOrder(
                Stubs.Domain.Languages.frenchAndEnglishLanguage.map {
                    equalTo<SelectableLanguage>(LanguageMapper().toUiModel(it))
                }
            )
        )
    }

    @Test
    fun get_empty_language_list_when_none_unavailable() {

        val viewModel = DictionaryRegistryViewModel(
            UiStubs.Uris.validUri,
            loadDbMetaInfoOnDb = getKoin().get(),
            registerNewDictionary = RegisterNewDictionarySuccessfullyMock(),
            dispatcherProvider = TestDispatcherProvider,
            listAvailableLanguages = ListLanguagesMock(ArrayList()),
            languageMapper = LanguageMapper(),
            addNewLanguage = MockRegisterLanguageUseCase()
        )

        assertThat(
            viewModel.getSelectableLanguages(),
            IsEmptyCollection()
        )
    }

    @Test
    fun add_new_language_on_new_language_registry_clicked() {

        // given that the available languages are english and french
        val languages = ArrayList(Stubs.Domain.Languages.frenchAndEnglishLanguage)
        val languageToAdd = UiStubs.TableUiInfo.langUiRu

        val viewModel = DictionaryRegistryViewModel(
            UiStubs.Uris.validUri,
            loadDbMetaInfoOnDb = getKoin().get(),
            registerNewDictionary = RegisterNewDictionarySuccessfullyMock(),
            dispatcherProvider = TestDispatcherProvider,
            listAvailableLanguages = ListLanguagesMock(languages),
            languageMapper = LanguageMapper(),
            addNewLanguage = MockRegisterLanguageUseCase(languages)
        )

        // when we register russian
        viewModel.onNewLanguageRegistryClicked(
            languageToAdd
        )

        // then russian is available
        assertThat(
            viewModel.getSelectableLanguages(),
            IsCollectionContaining(
                equalTo(languageToAdd)
            )
        )
    }

    @Test
    fun fail_on_new_language_registry_clicked_if_id_is_already_used() {

        // given that the available languages are english and french
        val languages = ArrayList(
            listOf(
                Stubs.Domain.Languages.french,
                Stubs.Domain.Languages.english
            )
        )
        val languageToAdd = SelectableLanguage.LanguageUi(
            code = Stubs.Domain.Languages.french.code,
            name = Stubs.Domain.Languages.french.name
        )

        val viewModel = DictionaryRegistryViewModel(
            UiStubs.Uris.validUri,
            loadDbMetaInfoOnDb = getKoin().get(),
            registerNewDictionary = RegisterNewDictionarySuccessfullyMock(),
            dispatcherProvider = TestDispatcherProvider,
            listAvailableLanguages = ListLanguagesMock(languages),
            languageMapper = LanguageMapper(),
            addNewLanguage = MockRegisterLanguageUseCase(languages)
        )

        // when we register french
        viewModel.onNewLanguageRegistryClicked(
            languageToAdd
        )

        // then operation fails
        assertThat(
            viewModel.error.value,
            not(IsNull())
        )
        assertThat(
            viewModel.registryError.value?.pureContent(),
            IsInstanceOf(RegistryError.LanguageIdDuplicate::class.java)
        )
    }
}