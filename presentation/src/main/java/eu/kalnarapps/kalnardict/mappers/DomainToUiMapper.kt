package eu.kalnarapps.kalnardict.mappers

interface DomainToUiMapper<DomainModel, UiModel> {

    fun toUiModel(domainModel: DomainModel): UiModel

}
