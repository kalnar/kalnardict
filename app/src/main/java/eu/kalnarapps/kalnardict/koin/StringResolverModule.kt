package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.android.utils.strings.KalnarStringResolver
import eu.kalnarapps.kalnardict.presentation.models.strings.StringResolver
import eu.kalnarapps.kalnardict.presentation.models.strings.StringResources
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val stringResolverModule = module {
    single(Qualifiers.StringResolvers.dictionaryQueryString) {
        KalnarStringResolver(
            androidContext()
        ) as StringResolver<StringResources.DictionaryQueryString>
    }
}

