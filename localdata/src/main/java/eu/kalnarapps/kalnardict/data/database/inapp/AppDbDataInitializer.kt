package eu.kalnarapps.kalnardict.data.database.inapp

interface AppDbDataInitializer {
    fun populateInitialData(db: AppDatabase)
}