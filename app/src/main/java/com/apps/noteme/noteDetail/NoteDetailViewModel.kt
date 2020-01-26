package com.apps.noteme.noteDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apps.noteme.database.Note
import com.apps.noteme.database.NoteDao
import kotlinx.coroutines.*

class NoteDetailViewModel(
    val noteDao: NoteDao,
    private val id: Long,
    application: Application
) : AndroidViewModel(application) {
//
//    private val _deleteNote = MutableLiveData<Boolean>()
//    val deleteNote:  LiveData<Boolean>
//        get() = _deleteNote

    private val _showSnackbar = MutableLiveData<Boolean>()
    val showSnackbar: LiveData<Boolean>
        get() = _showSnackbar

    val openedNote = MutableLiveData<Note>()

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                openedNote.postValue(noteDao.getNoteWithId(id))
            }
        }
    }

    fun onDelete() {
        uiScope.launch {
            delete(openedNote.value!!)
        }
    }


    fun onUpdate() {
        uiScope.launch {
            if (openedNote.value?.title.isNullOrBlank() && openedNote.value?.content.isNullOrBlank()) {
                showSnackbar()
                delete(openedNote.value!!)
            } else {
                update(openedNote.value!!)
            }
        }
    }

    private suspend fun delete(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.delete(note)
        }
    }

    private suspend fun update(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.update(note)
        }
    }

    fun showSnackbar() {
        _showSnackbar.value = true
    }

    fun showSnackbarcompleted() {
        _showSnackbar.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}