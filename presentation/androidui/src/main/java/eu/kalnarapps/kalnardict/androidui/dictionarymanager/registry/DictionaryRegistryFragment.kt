package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DictionaryRegistryFragment : Fragment() {

    private val args: DictionaryRegistryFragmentArgs by navArgs()
    private val registryViewModel: DictionaryRegistryViewModel by viewModel { parametersOf(args) }
}