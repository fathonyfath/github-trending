package id.fathonyfath.githubtrending.core

import id.fathonyfath.githubtrending.data.source.remote.model.ContributorResponse
import id.fathonyfath.githubtrending.model.Contributor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContributorResponseToContributorMapper
@Inject constructor() : Mapper<ContributorResponse, Contributor>() {
    override fun map(source: ContributorResponse): Contributor {
        return Contributor(
            username = source.username,
            url = source.url,
            avatar = source.avatar
        )
    }
}