package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.android.utils.strings.DictionaryQueryStringResolver
import eu.kalnarapps.kalnardict.android.utils.strings.DictionaryRenderingStrategyStringResolver
import eu.kalnarapps.kalnardict.presentation.models.strings.StringResolver
import eu.kalnarapps.kalnardict.presentation.models.strings.StringResources
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val stringResolverModule = module {
    single(Qualifiers.StringResolvers.dictionaryQueryString) {
        DictionaryQueryStringResolver(
            androidContext()
        ) as StringResolver<StringResources.DictionaryQueryString>
    }
    single(Qualifiers.StringResolvers.dictionaryRenderingStrategyStrings) {
        DictionaryRenderingStrategyStringResolver(
            androidContext()
        ) as StringResolver<StringResources.DictionaryRenderingStrategies>
    }
}

