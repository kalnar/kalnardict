package eu.kalnarapps.kalnardict.data.dao.query.formatter

interface QueryStringFormatter {
    fun format(queryString: String): String
}

abstract class SQLiteQueryStringFormatter : QueryStringFormatter {
    protected fun String.escapeSpecialChars(): String {
        return replace("[%_]".toRegex()) { specialChar ->
            "\\${specialChar.value}"
        }
    }
}

class ExactMatchQueryFormatter : SQLiteQueryStringFormatter() {
    override fun format(queryString: String): String {
        return if (queryString.isEmpty()) {
            "%"
        } else {
            queryString.escapeSpecialChars()
        }
    }
}

class FuzzyQueryFormatter : SQLiteQueryStringFormatter() {
    override fun format(queryString: String): String {
        return queryString.replace(".".toRegex()) {
            "%${it.value.escapeSpecialChars()}"
        } + "%"
    }
}

class MatchStartQueryFormatter : SQLiteQueryStringFormatter() {
    override fun format(queryString: String): String {
        return queryString.escapeSpecialChars() + "%"
    }
}

class MatchEndQueryFormatter : SQLiteQueryStringFormatter() {
    override fun format(queryString: String): String {
        return "%" + queryString.escapeSpecialChars()
    }
}

class MatchAnyWhereFormatter : SQLiteQueryStringFormatter() {
    override fun format(queryString: String): String {
        return if (queryString.isEmpty()) {
            "%"
        } else {
            "%${queryString.escapeSpecialChars()}%"
        }
    }
}
