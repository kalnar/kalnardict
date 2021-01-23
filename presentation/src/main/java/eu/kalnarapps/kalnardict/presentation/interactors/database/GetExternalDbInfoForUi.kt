package eu.kalnarapps.kalnardict.presentation.interactors.database

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.usecases.ReadExternalDbUseCase
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.ExternalTableUiInfo

class GetExternalDbInfoForUi(
    private val loadDbMetaInfoOnDb: ReadExternalDbUseCase
) : GetExternalDbInfoUseCaseForUi {
    override suspend operator fun invoke(uri: String): DataOperationResult<List<ExternalTableUiInfo>> {
        return loadDbMetaInfoOnDb(uri).map { list ->
            list.map { it.toExternalTableUiInfo() }
        }
    }
}

// TODO: refactor to DomainToUiModel mapper
private fun ExternalDatabaseTable.toExternalTableUiInfo(): ExternalTableUiInfo {
    return ExternalTableUiInfo(
        originalTableName = this.name,
        originalLanguageFrom = this.languageFrom,
        originalLanguageTo = this.languageTo
    )
}

