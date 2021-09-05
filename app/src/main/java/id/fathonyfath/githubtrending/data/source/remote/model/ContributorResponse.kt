package id.fathonyfath.githubtrending.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class ContributorResponse(
    @SerializedName("username") val username: String,
    @SerializedName("url") val url: String,
    @SerializedName("avatar") val avatar: String
)