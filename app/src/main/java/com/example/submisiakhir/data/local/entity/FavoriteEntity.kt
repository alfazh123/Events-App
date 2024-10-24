package com.example.submisiakhir.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
class FavoriteEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "summary")
    var summary: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "logo")
    var logo: String,
)