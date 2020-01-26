package com.apps.noteme.newNote

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apps.noteme.database.NoteDao

class NewNoteViewModelFactory(private val noteDao: NoteDao, private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewNoteViewModel::class.java)) {
            return NewNoteViewModel(noteDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}