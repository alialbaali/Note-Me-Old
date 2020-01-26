package com.apps.noteme.noteDetail


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.apps.noteme.R
import com.apps.noteme.database.AppDatabase
import com.apps.noteme.databinding.FragmentNoteDetailBinding
import com.google.android.material.snackbar.Snackbar

class NoteDetailFragment : Fragment() {
    private lateinit var binding: FragmentNoteDetailBinding
    private lateinit var noteDetailViewModel: NoteDetailViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_detail, container, false)
        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getInstance(application).noteDao

        val argumnets = NoteDetailFragmentArgs.fromBundle(arguments!!)

        val newNoteViewModelFactory =
            NoteDetailViewModelFactory(dataSource, argumnets.id, application)

        noteDetailViewModel = ViewModelProviders.of(this, newNoteViewModelFactory)
            .get(NoteDetailViewModel::class.java)

        binding.newNoteViewModel = noteDetailViewModel
//        binding.button.setOnClickListener {
//            noteDetailViewModel.onUpdate()
//            this.findNavController()
//                .navigate(NoteDetailFragmentDirections.actionViewNoteFragmentToStartFragment())
//        }

//        activity!!.supportFragmentManager.addOnBackStackChangedListener {
//            noteDetailViewModel.onUpdate()
//            this.findNavController()
//                .navigate(NoteDetailFragmentDirections.actionViewNoteFragmentToStartFragment())
//        }

        noteDetailViewModel.showSnackbar.observe(this, Observer {
            if (it == true) {
                Snackbar.make(view!!, "Empty note discarded", Snackbar.LENGTH_SHORT).show()
                noteDetailViewModel.showSnackbarcompleted()
            }
        })

//
//
        binding.lifecycleOwner = this
//
//        binding.note = newNoteViewModel.getNote(args.id)


        // Navigating to the start fragment after saving the note
//        newNoteViewModel.saveNoteNavigation.observe(this, Observer {
//            if(findNavController().currentDestination?.id == R.id.newNoteFragment){
//                findNavController().navigate(R.id.action_newNoteFragment_to_startFragment)
//            }
//            newNoteViewModel.saveNoteNavigationCompleted()
//        })

//        noteDetailViewModel.deleteNote.observe(this, Observer {
//            if (it) {
//                noteDetailViewModel.onDelete()
//                this.findNavController()
//                    .navigate(NoteDetailFragmentDirections.actionViewNoteFragmentToStartFragment())
//                Snackbar.make(view!!, "Note has been deleted", Snackbar.LENGTH_LONG).show()
//            }
//        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.note_menu, menu)
        // Check if the activity resolves
        if (null == getShareIntent().resolveActivity(activity!!.packageManager)) {
            // hide the menu item if it does'nt resolve
            menu?.findItem(R.id.share)?.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.delete -> onDelete()
            R.id.share -> shareSuccess()
            R.id.save -> onSave()
        }
        return super.onOptionsItemSelected(item)
    }



    private fun onDelete() {
        noteDetailViewModel.onDelete()
        this.findNavController()
            .navigate(NoteDetailFragmentDirections.actionViewNoteFragmentToStartFragment())
        Snackbar.make(view!!, "Note has been deleted", Snackbar.LENGTH_LONG).show()
    }

    private fun getShareIntent(): Intent {
        val title = noteDetailViewModel.openedNote.value?.title
        val content = noteDetailViewModel.openedNote.value?.content
        return ShareCompat.IntentBuilder.from(activity).setText(title + "\n\n" + content)
            .setType("text/plain").intent
    }

    // Starting an Activity with our new Intent
    private fun shareSuccess() {
        startActivity(getShareIntent())
    }

    private fun onSave() {
        noteDetailViewModel.onUpdate()
        this.findNavController()
            .navigate(NoteDetailFragmentDirections.actionViewNoteFragmentToStartFragment())
    }
}
