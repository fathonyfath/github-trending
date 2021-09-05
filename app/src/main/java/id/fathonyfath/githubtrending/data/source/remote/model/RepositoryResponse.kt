package id.fathonyfath.githubtrending.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class RepositoryResponse(
    @SerializedName("rank") val rank: Int,
    @SerializedName("username") val username: String,
    @SerializedName("repositoryName") val repositoryName: String,
    @SerializedName("url") val url: String,
    @SerializedName("description") val description: String?,
    @SerializedName("language") val language: String?,
    @SerializedName("languageColor") val languageColor: String?,
    @SerializedName("totalStars") val totalStars: Int,
    @SerializedName("forks") val forks: Int,
    @SerializedName("starsSince") val starsSince: Int,
    @SerializedName("since") val since: String,
    @SerializedName("builtBy") val builtBy: List<ContributorResponse>
)