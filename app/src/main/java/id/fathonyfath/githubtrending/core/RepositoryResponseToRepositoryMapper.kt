package id.fathonyfath.githubtrending.core

import id.fathonyfath.githubtrending.data.source.remote.model.RepositoryResponse
import id.fathonyfath.githubtrending.model.Repository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryResponseToRepositoryMapper
@Inject constructor(
    private val contributorResponseToContributorMapper: ContributorResponseToContributorMapper
) : Mapper<RepositoryResponse, Repository>() {
    override fun map(source: RepositoryResponse): Repository {
        return Repository(
            rank = source.rank,
            username = source.username,
            repositoryName = source.repositoryName,
            url = source.url,
            description = source.description,
            language = source.language,
            languageColor = source.languageColor,
            totalStars = source.totalStars,
            forks = source.forks,
            starsSince = source.starsSince,
            since = source.since,
            builtBy = source.builtBy.mapUsing(contributorResponseToContributorMapper)
        )
    }
}