package id.fathonyfath.githubtrending.data.source.remote

import id.fathonyfath.githubtrending.data.source.remote.model.RepositoryJson
import io.reactivex.Observable
import retrofit2.http.GET

interface TrendingApi {

    @GET("/repositories")
    fun getRepositories(): Observable<List<RepositoryJson>>
}