package eu.kalnarapps.kalnardict.androidui

import eu.kalnarapps.kalnardict.androidui.dictionarymanager.main.dictionaryManagerKoinModule
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.dictionaryRegistryKoinModule
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.dictionaryQueryKoinModule
import eu.kalnarapps.kalnardict.androidui.navigation.navigationKoinModule


val androidUiKoinModules = listOf(
    dictionaryManagerKoinModule,
    dictionaryRegistryKoinModule,
    dictionaryQueryKoinModule,
    navigationKoinModule
)