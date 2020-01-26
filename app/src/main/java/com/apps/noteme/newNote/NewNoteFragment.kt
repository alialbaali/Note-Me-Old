package com.apps.noteme.newNote


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
import com.apps.noteme.databinding.FragmentNewNoteBinding
import com.google.android.material.snackbar.Snackbar


class NewNoteFragment : Fragment() {
    private lateinit var binding: FragmentNewNoteBinding
    private lateinit var newNoteViewModel: NewNoteViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_note, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getInstance(application).noteDao

        val newNoteViewModelFactory = NewNoteViewModelFactory(dataSource, application)

        newNoteViewModel =
            ViewModelProviders.of(this, newNoteViewModelFactory).get(NewNoteViewModel::class.java)

        binding.newNoteViewModel = newNoteViewModel

        binding.lifecycleOwner = this


        newNoteViewModel.navigateToStart.observe(this, Observer {
            if (it == true) {
                newNoteViewModel.insert()
                this.findNavController()
                    .navigate(NewNoteFragmentDirections.actionNewNoteFragmentToStartFragment())
                newNoteViewModel.navigateToStartCompleted()
            }
        })

        newNoteViewModel.showSnackbar.observe(this, Observer {
            if (it == true) {
                Snackbar.make(view!!, "Note has been discarded", Snackbar.LENGTH_SHORT).show()
                newNoteViewModel.showSnackbarcompleted()
            }
        })
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
        newNoteViewModel.titleWatcher = null
        newNoteViewModel.contentWatcher = null
        newNoteViewModel.insert()
        this.findNavController()
            .navigate(NewNoteFragmentDirections.actionNewNoteFragmentToStartFragment())
    }

    private fun getShareIntent(): Intent {
        val title = newNoteViewModel.titleWatcher
        val content = newNoteViewModel.contentWatcher
        return ShareCompat.IntentBuilder.from(activity).setText(title + "\n\n" + content)
            .setType("text/plain").intent
    }

    // Starting an Activity with our new Intent
    private fun shareSuccess() {
        startActivity(getShareIntent())
    }

    private fun onSave() {
        newNoteViewModel.navigateToStart()
    }
}
