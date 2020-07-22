package eu.kalnarapps.kalnardict.interactors.test

import org.mockito.Mockito

inline fun <reified T> anyNonNull(): T = Mockito.any<T>(T::class.java)
