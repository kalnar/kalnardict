package eu.kalnarapps.kalnardict.presentation.mappers.database

import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalTableCreationJobInfo
import eu.kalnarapps.kalnardict.presentation.mappers.UiToDomainConverter
import eu.kalnarapps.kalnardict.presentation.models.mock.ImporterFormData

class ImporterFormDataMapper :
    UiToDomainConverter<ImporterFormData, ExternalTableCreationJobInfo, String> {

    override fun ImporterFormData.toDomain(
        complementaryData: String
    ): ExternalTableCreationJobInfo {
        return ExternalTableCreationJobInfo(
            dbPath = ExternalDatabase.LocalFile("${complementaryData}/${databaseName}"),
            table = ExternalDatabaseTable(
                name = this.tableName,
                languageFrom = this.sourceLanguage,
                languageTo = this.destinationLanguage
            ),
            size = DEFAULT_MOCK_TABLE_SIZE
        )
    }

    companion object {
        const val DEFAULT_MOCK_TABLE_SIZE = 5000
    }

}
