package com.example.movieproject.network.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "note")
data class MovieEntity(

    @Expose
    val title: String?,
    @Expose
    @SerializedName("overview")
    var overview: Int?,
    @Expose
    val note: String?,
    @Expose
    @SerializedName("poster_path")
    var imagePath: String?


) {
    // for diff util comparison
//    override fun equals(other: Any?): Boolean {
//        if (javaClass != other?.javaClass)
//            return false
//
//        other as MovieEntity
//
//        if (noteId != other.noteId)
//            return false
//        if (title != other.title)
//            return false
//        if (note != other.note)
//            return false
//        if (modifiedTime != other.modifiedTime)
//            return false
//
//        return true
//    }
}