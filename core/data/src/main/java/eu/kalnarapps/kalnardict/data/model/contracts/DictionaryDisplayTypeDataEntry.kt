package eu.kalnarapps.kalnardict.data.model.contracts

interface DictionaryDisplayTypeDataEntry {
    val id: String
}

data class UpdatedDisplayTypeDataEntry(override val id: String) : DictionaryDisplayTypeDataEntry