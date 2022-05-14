package eu.kalnarapps.kalnardict.presentation.interactors.database

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalTableCreationJobInfo
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.MockDatabaseInfo
import eu.kalnarapps.kalnardict.domain.usecases.mockdatabase.CreateExternalTableUseCase
import eu.kalnarapps.kalnardict.domain.usecases.mockdatabase.GetMockDatabaseSettingsUseCase
import eu.kalnarapps.kalnardict.presentation.interactors.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.presentation.mappers.UiToDomainConverter
import eu.kalnarapps.kalnardict.presentation.models.mock.ImporterFormData
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub

class CreateMockTableFromUiTest {

    private val getMockDatabaseInfo: GetMockDatabaseSettingsUseCase =
        mock(GetMockDatabaseSettingsUseCase::class.java)
    private val createExternalTableUseCase = mock(CreateExternalTableUseCase::class.java)
    private val uiToDomainConverter =
        mock(UiToDomainConverter::class.java)
                as UiToDomainConverter<ImporterFormData, ExternalTableCreationJobInfo, String>

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()


    @Test
    fun `Given mock database settings are valid, when creating external table successfully, then return db path`() {

        testCoroutineRule.runBlockingTest {

            // given we have a valid mock database info path
            val givenDirPath = "mock://path/dir"
            getMockDatabaseInfo.stub {
                onBlocking { invoke() }.doReturn(
                    DataOperationResult.Success(
                        MockDatabaseInfo(
                            givenDirPath
                        )
                    )
                )
            }

            val givenUiModel = ImporterFormData(
                databaseName = "dbName",
                tableName = "tableName",
                sourceLanguage = "sourceLanguage",
                destinationLanguage = "destinationLanguage"
            )

            val mappedModel = ExternalTableCreationJobInfo(
                dbPath = ExternalDatabase.LocalFile("$givenDirPath/dbName"),
                table = ExternalDatabaseTable(
                    name = "tableName",
                    languageFrom = "sourceLanguage",
                    languageTo = "destinationLanguage"
                ),
                size = 5000
            )

            `when`(
                with(uiToDomainConverter) {
                    givenUiModel.toDomain(givenDirPath)
                }
            ).thenReturn(mappedModel)

            createExternalTableUseCase.stub {
                onBlocking { invoke(mappedModel) }.thenReturn(OperationResult.Success)
            }

            val useCase = CreateMockTableFromUi(
                getMockDatabaseInfo = getMockDatabaseInfo,
                createExternalTableUseCase = createExternalTableUseCase,
                uiToDomainConverter = uiToDomainConverter
            )

            val createMockTableFromUiResult = useCase.invoke(givenUiModel)

            assertThat(
                createMockTableFromUiResult,
                equalTo(DataOperationResult.Success("$givenDirPath/dbName"))
            )

        }

    }
}