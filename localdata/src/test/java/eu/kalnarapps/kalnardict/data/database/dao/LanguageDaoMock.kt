package eu.kalnarapps.kalnardict.data.database.dao

import eu.kalnarapps.kalnardict.data.dao.DaoConstants
import eu.kalnarapps.kalnardict.data.dao.LanguageDao
import eu.kalnarapps.kalnardict.data.entities.Language


class LanguageDaoMock(
    private val languages: ArrayList<Language> = ArrayList(
        listOf<Language>(
            Languages.english,
            Languages.french
        )
    )
) : LanguageDao {

    override suspend fun getLanguageById(idString: String): Language? {
        return languages.find { it.id == idString }
    }

    override suspend fun getLanguages(): List<Language> {
        return languages
    }

    override suspend fun insertLanguage(language: Language): Long {
        return if (languages.any { it.id == language.id }) {
            DaoConstants.ROOM_ON_CONFLICT_IGNORE_CONSTANT
        } else {
            languages.add(language)
            1
        }
    }
}