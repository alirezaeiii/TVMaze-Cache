package com.android.sample.tvmaze.domain

import android.os.Parcelable
import com.android.sample.tvmaze.database.DatabaseImage
import com.android.sample.tvmaze.database.DatabaseShow
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Show(
    val id: Long,
    val name: String,
    val image: Image,
    val summary: String
) : Parcelable

@Parcelize
class Image(
    val medium: String,
    val original: String
) : Parcelable

fun List<Show>.asDatabaseModel(): Array<DatabaseShow> {
    return map {
        DatabaseShow(
            id = it.id,
            name = it.name,
            image = DatabaseImage(
                medium = it.image.medium,
                original = it.image.original
            ),
            summary = it.summary
        )
    }.toTypedArray()
}