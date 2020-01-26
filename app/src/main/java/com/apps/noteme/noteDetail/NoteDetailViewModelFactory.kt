package com.apps.noteme.noteDetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apps.noteme.database.NoteDao


class NoteDetailViewModelFactory(
    private val dataSource: NoteDao,
    private val id: Long,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteDetailViewModel::class.java)) {
            return NoteDetailViewModel(dataSource, id, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}