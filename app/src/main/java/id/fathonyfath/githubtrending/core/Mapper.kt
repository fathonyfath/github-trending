package id.fathonyfath.githubtrending.core

abstract class Mapper<T, R> {
    abstract fun map(source: T): R
    fun map(sources: Iterable<T>): List<R> {
        return sources.map(::map)
    }
}

fun <T, R> T.mapUsing(mapper: Mapper<T, R>): R {
    return mapper.map(this)
}

fun <T, R> Iterable<T>.mapUsing(mapper: Mapper<T, R>): List<R> {
    return mapper.map(this)
}