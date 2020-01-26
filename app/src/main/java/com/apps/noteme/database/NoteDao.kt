package com.apps.noteme.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Insert
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteWithId(id: Long): Note

    @Query("SELECT * FROM notes")
    fun getAll(): LiveData<List<Note>>

    @Delete
    fun delete(note: Note)

    @Query("DELETE FROM notes")
    fun deleteAll()
}