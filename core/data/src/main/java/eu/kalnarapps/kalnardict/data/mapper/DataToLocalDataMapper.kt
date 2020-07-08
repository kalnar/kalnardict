package eu.kalnarapps.kalnardict.data.mapper

interface DomainToLocalDataMapper<DomainModel, LocalData> {

    fun toLocalData(domainModel: DomainModel): LocalData
    fun toDomainModel(localData: LocalData): DomainModel

}