package eu.kalnarapps.kalnardict.data.database.dao

import eu.kalnarapps.kalnardict.data.dao.LanguageDao
import eu.kalnarapps.kalnardict.data.entities.Language


class LanguageDaoMock : LanguageDao {
    private val languages = listOf<Language>(
        Languages.english,
        Languages.french
    )

    override suspend fun getLanguageById(idString: String): Language? {
        return languages.find { it.id == idString }
    }
}