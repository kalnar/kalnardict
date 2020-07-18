package eu.kalnarapps.kalnardict.data.mapper

import eu.kalnarapps.kalnardict.data.ImportEntry
import eu.kalnarapps.kalnardict.data.ImportEntryBatch
import eu.kalnarapps.kalnardict.data.model.ImportEntryBatchData
import eu.kalnarapps.kalnardict.data.model.ImportEntryData
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob

fun ImportJob.toImportEntry(fromId: Int, tillId: Int): ImportEntryBatch {
    return ImportEntryBatchData(
        externalDictionaryResource = resource.toExternalDictionaryResource(),
        tableInfo = table.toTableInfo(),
        fromRowId = fromId,
        tillRowId = tillId
    )
}

fun ImportJob.toImportEntry(): ImportEntry {
    return ImportEntryData(
        externalDictionaryResource = resource.toExternalDictionaryResource(),
        tableInfo = table.toTableInfo()
    )
}

