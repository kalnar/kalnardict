package eu.kalnarapps.kalnardict.data.mapper

interface DomainToDataMapper<DomainModel, DataModel> {

    fun toData(domainModel: DomainModel): DataModel

}