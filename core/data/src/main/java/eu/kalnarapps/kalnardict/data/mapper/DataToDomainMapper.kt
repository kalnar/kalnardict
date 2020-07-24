package eu.kalnarapps.kalnardict.data.mapper

interface DataToDomainMapper<DataModel, DomainModel> {

    fun toDomainModel(localData: DataModel): DomainModel
}