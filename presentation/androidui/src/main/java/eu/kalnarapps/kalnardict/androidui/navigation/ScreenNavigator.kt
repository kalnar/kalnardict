package eu.kalnarapps.kalnardict.androidui.navigation

import java.net.URI

interface ScreenNavigator {

    fun navigateToDictionaryRegistry(dbUri: URI)
}