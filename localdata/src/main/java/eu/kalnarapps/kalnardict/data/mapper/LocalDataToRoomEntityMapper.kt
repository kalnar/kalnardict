package eu.kalnarapps.kalnardict.data.mapper

interface LocalDataToRoomEntityMapper<LocalData, RoomEntityModel> {

    fun toLocalData(roomEntity: RoomEntityModel): LocalData
    fun toRoomEntityModel(localData: LocalData): RoomEntityModel

}