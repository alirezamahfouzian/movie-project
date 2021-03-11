package com.example.movieproject.network.entity

import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "note")
data class MovieEntity(

    @Expose
    @SerializedName("status_message")
    val status_message: String?,
    @SerializedName("status_code")
    @Expose
    val status_code: String?,
    @Expose
    val id: String?,
    @Expose
    val title: String?,
    @Expose
    var overview: String?,
    @Expose
    @SerializedName("poster_path")
    var imagePath: String?,
    @Expose
    @SerializedName("vote_average")
    var voteAverage: String?


) {
    // for diff util comparison
    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass)
            return false
        other as MovieEntity
        if (overview != other.overview)
            return false
        if (title != other.title)
            return false
        if (overview != other.overview)
            return false
        if (imagePath != other.imagePath)
            return false
        if (voteAverage != other.voteAverage)
            return false

        return true
    }
}