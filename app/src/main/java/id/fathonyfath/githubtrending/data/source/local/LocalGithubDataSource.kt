package id.fathonyfath.githubtrending.data.source.local

import id.fathonyfath.githubtrending.data.cache.RepositoriesCache
import id.fathonyfath.githubtrending.data.source.GithubDataSource
import id.fathonyfath.githubtrending.model.Repository
import id.fathonyfath.githubtrending.scheduler.SchedulerProvider
import io.reactivex.Observable
import javax.inject.Inject

class LocalGithubDataSource
@Inject constructor(
    private val repositoriesCache: RepositoriesCache,
    private val schedulerProvider: SchedulerProvider
) : GithubDataSource {
    override fun popularRepositories(): Observable<List<Repository>> {
        return repositoriesCache.get()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
    }

}