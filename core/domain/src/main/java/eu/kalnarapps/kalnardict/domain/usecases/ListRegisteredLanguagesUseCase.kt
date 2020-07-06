package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage

interface ListRegisteredLanguagesUseCase {
    suspend operator fun invoke(): List<DictLanguage>
}