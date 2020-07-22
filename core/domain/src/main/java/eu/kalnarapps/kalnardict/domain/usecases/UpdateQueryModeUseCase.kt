package eu.kalnarapps.kalnardict.domain.usecases

interface UpdateQueryModeUseCase {
    suspend operator fun invoke(queryModeId: Int)
}