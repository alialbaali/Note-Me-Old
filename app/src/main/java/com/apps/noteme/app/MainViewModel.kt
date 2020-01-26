package com.apps.noteme.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.apps.noteme.database.AppDatabase
import com.apps.noteme.database.NoteDao

class MainViewModel(private val noteDao: NoteDao, application: Application) :
    AndroidViewModel(application) {

    val notes = noteDao.getAll()

    val _changeLayoutManager = MutableLiveData<RecyclerView.LayoutManager>()

//    private val _changeToGridLayout = MutableLiveData<Boolean>()
//    val changeToGridLayout: LiveData<Boolean>
//        get() = _changeToGridLayout
//
//    private val _changeToLinearLayout = MutableLiveData<Boolean>()
//    val changeToLinearLayout: LiveData<Boolean>
//        get() = _changeToLinearLayout
//
//    fun changeToLinearLayout() {
//        _changeToLinearLayout.value = true
//    }
//
//    fun changeToGridLayout() {
//        _changeToGridLayout.value = true
//    }
//
//    fun changeToLinearLayoutCompleted() {
//        _changeToLinearLayout.value = null
//    }
//
//    fun changeToGridLayoutCompleted() {
//        _changeToLinearLayout.value = null
//    }

    private val _navigateToNoteDetail = MutableLiveData<Long>()
    val navigateToNoteDetail: LiveData<Long>
        get() = _navigateToNoteDetail

    fun onNoteClicked(id: Long) {
        _navigateToNoteDetail.value = id
    }

    fun onNoteClickedCompleted() {
        _navigateToNoteDetail.value = null
    }

    private val _navigateToNewNote = MutableLiveData<Boolean>()
    val navigateToNewNote: LiveData<Boolean>
        get() = _navigateToNewNote

    fun navigateToNewNote() {
        _navigateToNewNote.value = true
    }

    fun navigateToNewNoteCompleted() {
        _navigateToNewNote.value = null
    }
}
