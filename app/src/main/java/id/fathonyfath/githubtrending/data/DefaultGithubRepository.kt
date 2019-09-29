package id.fathonyfath.githubtrending.data

import id.fathonyfath.githubtrending.data.source.GithubDataSource
import id.fathonyfath.githubtrending.di.AppComponent
import id.fathonyfath.githubtrending.model.Repository
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

class DefaultGithubRepository
@Inject constructor(
    @Named(AppComponent.LOCAL_DATA_SOURCE) localDataSource: GithubDataSource,
    @Named(AppComponent.REMOTE_DATA_SOURCE) remoteDataSource: GithubDataSource
) : GithubRepository {
    override fun repositories(): Observable<List<Repository>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}