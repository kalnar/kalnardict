package eu.kalnarapps.kalnardict.data.database.dao

import eu.kalnarapps.kalnardict.data.dao.LanguageDao
import eu.kalnarapps.kalnardict.data.entities.Language


class LanguageDaoMock(
    private val languages: List<Language> = listOf<Language>(
        Languages.english,
        Languages.french
    )
) : LanguageDao {

    override suspend fun getLanguageById(idString: String): Language? {
        return languages.find { it.id == idString }
    }

    override suspend fun getLanguages(): List<Language> {
        return languages
    }
}