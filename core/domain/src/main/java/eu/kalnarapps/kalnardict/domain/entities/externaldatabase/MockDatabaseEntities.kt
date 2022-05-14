package eu.kalnarapps.kalnardict.domain.entities.externaldatabase

data class MockDatabaseInfo(
    val directoryPath: String
)

data class ExternalTableCreationJobInfo(
    val dbPath: ExternalDatabase.LocalFile,
    val table: ExternalDatabaseTable,
    val size: Int
)