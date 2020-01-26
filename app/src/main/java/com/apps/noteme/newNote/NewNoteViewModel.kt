package com.apps.noteme.newNote

import android.app.Application
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apps.noteme.database.Note
import com.apps.noteme.database.NoteDao
import kotlinx.coroutines.*

class NewNoteViewModel(private val noteDao: NoteDao, application: Application) : AndroidViewModel(application) {

//    val note = MutableLiveData<Note>()
//
//    init {
//        Timber.i("init block lol")
//        note.value?.title = ""
//        note.value?.content = ""
//    }

    private val _showSnackbar = MutableLiveData<Boolean>()
    val showSnackbar: LiveData<Boolean>
        get() = _showSnackbar

    fun showSnackbar() {
        _showSnackbar.value = true
    }

    fun showSnackbarcompleted() {
        _showSnackbar.value = null
    }

    private val viewModelJob = Job()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToStart = MutableLiveData<Boolean>()
    val navigateToStart: LiveData<Boolean>
        get() = _navigateToStart

    fun navigateToStart() {
        _navigateToStart.value = true
    }

    fun navigateToStartCompleted() {
        _navigateToStart.value = null
    }

    fun insert() {
        coroutineScope.launch {
            val newNote = Note()
            newNote.title = titleWatcher
            newNote.content = contentWatcher
            if (newNote.title.isNullOrBlank() && newNote.content.isNullOrBlank()) {
                showSnackbar()
            } else {
                insert(newNote)
            }
        }
    }


    private suspend fun insert(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.insert(note)
        }
    }



    var titleWatcher: String? = null
    var mTitle = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(title: Editable?) {
            titleWatcher = title.toString()
        }
    }

    var contentWatcher: String? = null
    var mContent = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(content: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(content: Editable?) {
            contentWatcher = content.toString()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}