package eu.kalnarapps.kalnardict.presentation.interactors.query

import eu.kalnarapps.kalnardict.domain.usecases.UpdateQueryModeUseCase

class UpdateQueryModeFromUi(
    private val updateQueryModeUseCase: UpdateQueryModeUseCase
) : UpdateQueryModeUseCaseFromUi {
    override suspend operator fun invoke(queryModeId: Int) {
        updateQueryModeUseCase(queryModeId)
    }
}