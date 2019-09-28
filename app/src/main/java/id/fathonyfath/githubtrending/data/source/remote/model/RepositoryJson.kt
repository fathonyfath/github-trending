package id.fathonyfath.githubtrending.data.source.remote.model

data class RepositoryJson(
    val author: String,
    val name: String,
    val avatar: String,
    val url: String,
    val description: String,
    val language: String?,
    val languageColor: String?,
    val stars: Long,
    val forks: Long,
    val currentPeriodStars: Long,
    val builtBy: List<ContributorJson>
)