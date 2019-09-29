package id.fathonyfath.githubtrending.model

data class Repository(
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
    val builtBy: List<Contributor>
)