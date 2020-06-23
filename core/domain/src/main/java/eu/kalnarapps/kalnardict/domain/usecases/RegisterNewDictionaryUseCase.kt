package eu.kalnarapps.kalnardict.domain.usecases

interface RegisterNewDictionaryUseCase {
    suspend operator fun invoke(
        dbUri: String,
        originalName: String,
        savingName: String,
        languageFrom: String,
        languageTo: String
    )
}