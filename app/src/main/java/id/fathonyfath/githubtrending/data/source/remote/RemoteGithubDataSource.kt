package id.fathonyfath.githubtrending.data.source.remote

import id.fathonyfath.githubtrending.scheduler.SchedulerProvider
import id.fathonyfath.githubtrending.data.source.GithubDataSource
import id.fathonyfath.githubtrending.data.source.remote.model.RepositoryJson
import io.reactivex.Observable
import javax.inject.Inject

class RemoteGithubDataSource
@Inject constructor(
    private val trendingApi: TrendingApi,
    private val schedulerProvider: SchedulerProvider
) : GithubDataSource {

    override fun popularRepositories(): Observable<List<RepositoryJson>> {
        return trendingApi.getRepositories()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
    }

}