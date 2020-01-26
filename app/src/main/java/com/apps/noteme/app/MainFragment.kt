package com.apps.noteme.app


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.noteme.R
import com.apps.noteme.database.AppDatabase
import com.apps.noteme.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getInstance(application).noteDao

        val mainViewModelFactory = MainViewModelFactory(dataSource, application)

        val mainViewModel =
            ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel::class.java)

        binding.mainViewModel = mainViewModel

        binding.lifecycleOwner = this

        setHasOptionsMenu(true)

        var adapter = AppRecyclerViewAdapter(NoteListener { id ->
            mainViewModel.onNoteClicked(id)
        })

        val itemDecorator = DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.divider)!!)

        binding.appRecyclerView.adapter = adapter

        binding.appRecyclerView.addItemDecoration(itemDecorator)
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("Layout")) {
                Log.i("MainFragment", "LinearLayoutManager ")
                binding.appRecyclerView.layoutManager = LinearLayoutManager(context)
            } else {
                Log.i("MainFragment", "GridLayoutManager")
                binding.appRecyclerView.layoutManager = GridLayoutManager(context, 2)
            }
        } else {
            binding.appRecyclerView.layoutManager = LinearLayoutManager(context)
        }
        mainViewModel.notes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })


        mainViewModel.navigateToNoteDetail.observe(this, Observer { id ->
            id?.let {
                this.findNavController().navigate(
                    MainFragmentDirections.actionStartFragmentToViewNoteFragment(id)
                )
                mainViewModel.onNoteClickedCompleted()
            }
        })
        binding.fab.setOnClickListener {
            mainViewModel.navigateToNewNote()
        }

        mainViewModel.navigateToNewNote.observe(this, Observer {
            if (it == true) {
                this.findNavController().navigate(R.id.action_startFragment_to_newNoteFragment)
                mainViewModel.navigateToNewNoteCompleted()
            }
        })


//        mainViewModel.changeToLinearLayout.observe(this, Observer {
//            if (it == true) {
//                changeToLinearLayout()
//                mainViewModel.changeToLinearLayoutCompleted()
//            }
//        })
//        mainViewModel.changeToGridLayout.observe(this, Observer {
//            if (it == true) {
//                changeToGridLayout()
//                mainViewModel.changeToGridLayoutCompleted()
//            }
//        })


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val layoutManager = binding.appRecyclerView.layoutManager
        if (layoutManager is LinearLayoutManager && layoutManager !is GridLayoutManager) {
            changeToGridLayout()
        } else {
            changeToLinearLayout()
        }
        return true
    }

    private fun changeToGridLayout() {
        binding.appRecyclerView.layoutManager = GridLayoutManager(context, 2)
    }

    private fun changeToLinearLayout() {
        binding.appRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (binding.appRecyclerView.layoutManager is LinearLayoutManager) {
            Log.i("MainFragment", "=setter 1 ")
            outState.putBoolean("Layout", true)
        } else {
            Log.i("MainFragment", "setter 2 ")
            outState.putBoolean("Layout", false)
        }
    }


}

