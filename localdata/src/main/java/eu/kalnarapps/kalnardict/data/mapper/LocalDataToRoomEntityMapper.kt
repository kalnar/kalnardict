package eu.kalnarapps.kalnardict.data.mapper

interface LocalDataToRoomEntityMapper<LocalData, RoomEntityModel> {

    fun toRoomEntityModel(localData: LocalData): RoomEntityModel

}

interface RoomEntityToLocalDataMapper<RoomEntityModel, LocalData> {

    fun toLocalData(roomEntity: RoomEntityModel): LocalData

}
