package eu.kalnarapps.kalnardict.data.mapper

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult

interface DataToDomainOperationalMapper<DataModel, DomainModel> {

    suspend fun toDomainModel(localData: DataModel): DataOperationResult<DomainModel>

}
