package eu.kalnarapps.kalnardict.presentation.mappers

interface UiToDomainMapper<UiModel, DomainModel> {

    fun toDomainModel(uiModel: UiModel): DomainModel


}

interface UiToDomainConverter<UiModel, DomainModel, ComplementaryData> {
    fun UiModel.toDomain(complementaryData: ComplementaryData): DomainModel
}

interface UiToDomainMapperWithExtras<UiModel, DomainModel, Extra> {

    fun toDomainModel(uiModel: UiModel, extra: Extra): DomainModel

}
