package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.mapper

interface DomainToUiMapper<DomainModel, UiModel> {

    fun toUiModel(domainModel: DomainModel): UiModel

}