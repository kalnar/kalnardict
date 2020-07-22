package eu.kalnarapps.kalnardict.androidui.common.mapper

interface DomainToUiMapper<DomainModel, UiModel> {

    fun toUiModel(domainModel: DomainModel): UiModel

}

interface UiToDomainMapper<UiModel, DomainModel> {

    fun toDomainModel(uiModel: UiModel): DomainModel

}
