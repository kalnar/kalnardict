package eu.kalnarapps.kalnardict.data

interface GetMockDatabaseSettingsRepository {
    suspend fun getMockDatabaseDirectoryPath(): String
}