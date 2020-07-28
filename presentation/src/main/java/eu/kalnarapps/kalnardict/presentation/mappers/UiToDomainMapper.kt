package eu.kalnarapps.kalnardict.presentation.mappers

interface UiToDomainMapper<UiModel, DomainModel> {

    fun toDomainModel(uiModel: UiModel): DomainModel

}
