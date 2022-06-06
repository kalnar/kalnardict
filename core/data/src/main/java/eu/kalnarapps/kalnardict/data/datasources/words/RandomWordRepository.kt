package eu.kalnarapps.kalnardict.data.datasources.words

class RandomWordRepository {
    fun getRandomWord(): String = "random"
    fun getRandomWord(id: Int): String = "random $id"
    fun getRandomWords(ids: List<Int>): List<String> = ids.map { "random $it" }
}