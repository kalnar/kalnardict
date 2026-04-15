package eu.kalnarapps.kalnardict.androidui.dependencies

import eu.kalnarapps.kalnardict.androidui.navigationscreen.NavigationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val navigationScreenKoinMockModule = module {
    viewModel { NavigationViewModel() }
}