package com.apps.noteme.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "name")
    var name: String? = null,
    @ColumnInfo(name = "email")
    var email: String? = null
)