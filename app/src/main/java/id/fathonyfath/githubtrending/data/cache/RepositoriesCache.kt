package id.fathonyfath.githubtrending.data.cache

import id.fathonyfath.githubtrending.model.Repository
import io.reactivex.Completable
import io.reactivex.Observable

interface RepositoriesCache {

    fun get(): Observable<List<Repository>>

    fun put(repositories: List<Repository>): Completable

    fun isCached(): Observable<Boolean>

    fun isExpired(): Observable<Boolean>

    fun evictAll(): Completable
}