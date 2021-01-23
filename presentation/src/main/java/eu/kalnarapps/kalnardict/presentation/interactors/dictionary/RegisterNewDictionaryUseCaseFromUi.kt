package eu.kalnarapps.kalnardict.presentation.interactors.dictionary

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportProgress
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.ImportTableProgress
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.NewDictionaryInfoUi
import kotlinx.coroutines.flow.Flow

interface RegisterNewDictionaryUseCaseFromUi {
    suspend operator fun invoke(
        newDictionaryInfoUi: NewDictionaryInfoUi
    ): Flow<DataOperationResult<ImportTableProgress>>
}