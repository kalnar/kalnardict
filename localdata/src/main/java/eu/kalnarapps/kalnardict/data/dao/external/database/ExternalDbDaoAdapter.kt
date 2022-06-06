package eu.kalnarapps.kalnardict.data.dao.external.database

import eu.kalnarapps.kalnardict.data.datasources.database.DatabaseMetaDataSource
import eu.kalnarapps.kalnardict.data.entities.ExternalDatabase

class ExternalDbDaoAdapter(
    private val externalDbDao: ExternalDbDao
) : DatabaseMetaDataSource {

    override suspend fun addDatabase(path: String) {
        externalDbDao.insertDatabase(ExternalDatabase(path))
    }

}