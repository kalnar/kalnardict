package eu.kalnarapps.kalnardict.data.model

import eu.kalnarapps.kalnardict.data.ExternalDictionaryResource
import eu.kalnarapps.kalnardict.data.ImportEntry
import eu.kalnarapps.kalnardict.data.ImportEntryBatch

data class TestImportEntry(
    override val externalDictionaryResource: ExternalDictionaryResource,
    override val tableInfo: ImportEntry.TableInfo
) : ImportEntry

data class TestImportEntryBatch(
    override val externalDictionaryResource: ExternalDictionaryResource,
    override val tableInfo: ImportEntry.TableInfo,
    override val fromRowId: Int,
    override val tillRowId: Int
) : ImportEntryBatch
