package id.fathonyfath.githubtrending.data

import android.util.Log
import id.fathonyfath.githubtrending.data.cache.CacheNotFoundException
import id.fathonyfath.githubtrending.data.source.GithubDataSource
import id.fathonyfath.githubtrending.di.AppComponent
import id.fathonyfath.githubtrending.model.Repository
import id.fathonyfath.githubtrending.scheduler.SchedulerProvider
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

class DefaultGithubRepository
@Inject constructor(
    @Named(AppComponent.LOCAL_DATA_SOURCE) private val localDataSource: GithubDataSource,
    @Named(AppComponent.REMOTE_DATA_SOURCE) private val remoteDataSource: GithubDataSource,
    private val schedulerProvider: SchedulerProvider
) : GithubRepository {

    override fun repositories(): Observable<List<Repository>> {
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