package id.fathonyfath.githubtrending.model

data class Repository(
    val rank: Int,
    val username: String,
    val repositoryName: String,
    val url: String,
    val description: String?,
    val language: String?,
    val languageColor: String?,
    val totalStars: Int,
    val forks: Int,
    val starsSince: Int,
    val since: String,
    val builtBy: List<Contributor>
)