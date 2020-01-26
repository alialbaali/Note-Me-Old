package com.apps.noteme.app

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apps.noteme.database.NoteDao

class MainViewModelFactory(
    private val dataSource: NoteDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}