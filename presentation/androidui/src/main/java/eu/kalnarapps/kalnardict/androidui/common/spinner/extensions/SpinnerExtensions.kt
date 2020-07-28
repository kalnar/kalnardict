package eu.kalnarapps.kalnardict.androidui.common.spinner.extensions


fun <T> List<T>.withItemOnFirstIndex(currentItem: T): List<T> {
    return listOf(currentItem).plus(this.filterNot { it == currentItem })
}