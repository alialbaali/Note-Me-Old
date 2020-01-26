package com.apps.noteme.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "user_id")
    var user_id: Int? = null,
    @ColumnInfo(name = "title")
    var title: String? = null,
    @ColumnInfo(name = "content")
    var content: String? = null
)