package eu.kalnarapps.kalnardict.presentation.mappers

interface DomainToUiMapper<DomainModel, UiModel> {

    fun toUiModel(domainModel: DomainModel): UiModel

}
