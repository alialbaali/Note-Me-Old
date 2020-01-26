package com.apps.noteme.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Query("SELECT * FROM users WHERE id = :id")
    fun get(id: Int): User?

    @Query("SELECT * FROM users")
    fun getAll(): User?
}