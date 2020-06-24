package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.usecases.ReadExternalDbUseCase
import java.net.URI

class ListMetaInfoOnDb(
    private val dictionaryRepository: DictionaryRepository
) : ReadExternalDbUseCase {
    override suspend fun invoke(uri: URI): DataOperationResult<List<ExternalDatabaseTable>> {
        return dictionaryRepository.readMetaInfoFromExternalDb(
            externalDatabase = ExternalDatabase.LocalFile(
                uri = uri
            )
        )
    }
}