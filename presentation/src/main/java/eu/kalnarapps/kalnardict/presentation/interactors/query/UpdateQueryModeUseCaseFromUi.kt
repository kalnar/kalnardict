package eu.kalnarapps.kalnardict.presentation.interactors.query

interface UpdateQueryModeUseCaseFromUi {
    suspend operator fun invoke(queryModeId: Int)
}