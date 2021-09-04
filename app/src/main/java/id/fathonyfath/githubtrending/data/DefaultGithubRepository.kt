package id.fathonyfath.githubtrending.data

import id.fathonyfath.githubtrending.data.cache.CacheNotFoundException
import id.fathonyfath.githubtrending.data.source.GithubDataSource
import id.fathonyfath.githubtrending.di.LocalDataSource
import id.fathonyfath.githubtrending.di.RemoteDataSource
import id.fathonyfath.githubtrending.model.Repository
import id.fathonyfath.githubtrending.scheduler.SchedulerProvider
import io.reactivex.Observable
import javax.inject.Inject

class DefaultGithubRepository
@Inject constructor(
    @LocalDataSource private val localDataSource: GithubDataSource,
    @RemoteDataSource private val remoteDataSource: GithubDataSource,
    private val schedulerProvider: SchedulerProvider
) : GithubRepository {

    override fun repositories(clearCache: Boolean): Observable<List<Repository>> {
        if (clearCache) {
            return remoteDataSource.popularRepositories()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
        } else {
            return localDataSource.popularRepositories()
                .onErrorResumeNext { throwable: Throwable ->
                    if (throwable is CacheNotFoundException) {
                        return@onErrorResumeNext remoteDataSource.popularRepositories()
                    }

                    throw throwable
                }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
        }

    }

}