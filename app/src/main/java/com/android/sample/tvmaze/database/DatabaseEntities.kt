package com.android.sample.tvmaze.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.sample.tvmaze.domain.Image
import com.android.sample.tvmaze.domain.Show

@Entity
class DatabaseShow(
    @PrimaryKey val id: Long,
    val name: String,
    @Embedded val image: DatabaseImage,
    val summary: String
)

class DatabaseImage(
    val medium: String,
    val original: String
)

fun List<DatabaseShow>.asDomainModel(): List<Show> {
    return map {
        Show(
            id = it.id,
            name = it.name,
            image = Image(
                medium = it.image.medium,
                original = it.image.original
            ),
            summary = it.summary
        )
    }
}