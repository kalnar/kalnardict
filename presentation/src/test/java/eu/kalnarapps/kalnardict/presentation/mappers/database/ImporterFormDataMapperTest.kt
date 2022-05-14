package eu.kalnarapps.kalnardict.presentation.mappers.database

import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalTableCreationJobInfo
import eu.kalnarapps.kalnardict.presentation.models.mock.ImporterFormData
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat

class ImporterFormDataMapperTest {

    private val uiToDomainConverter = ImporterFormDataMapper()

    @Test
    fun `Given an ui model, when converting to domain, then concat dir path with table name`() {

        val givenDirPath = "mock://path/dir"
        val givenUiModel = ImporterFormData(
            databaseName = "dbName",
            tableName = "tableName",
            sourceLanguage = "sourceLanguage",
            destinationLanguage = "destinationLanguage"
        )

        val expectedDomainModel = ExternalTableCreationJobInfo(
            dbPath = ExternalDatabase.LocalFile("$givenDirPath/dbName"),
            table = ExternalDatabaseTable(
                name = "tableName",
                languageFrom = "sourceLanguage",
                languageTo = "destinationLanguage"
            ),
            size = 5000
        )

        val actualDomainModel = with(uiToDomainConverter) {
            givenUiModel.toDomain(givenDirPath)
        }

        assertThat(
            actualDomainModel,
            equalTo(expectedDomainModel)
        )
    }
}