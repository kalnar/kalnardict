package eu.kalnarapps.kalnardict.data.mapper

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult

interface DomainToDataMapper<DomainModel, DataModel> {

    fun toData(domainModel: DomainModel): DataModel

}

interface DataToDomainMapper<DataModel, DomainModel> {

    fun toDomainModel(localData: DataModel): DomainModel
}

interface DataToDomainOperationalMapper<DataModel, DomainModel> {

    suspend fun toDomainModel(localData: DataModel): DataOperationResult<DomainModel>

}
