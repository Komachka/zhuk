package com.evo.evozhuk.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.evo.evozhuk.R
import androidx.recyclerview.widget.DividerItemDecoration
import com.evo.evozhuk.utils.observe
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.android.synthetic.main.fragment_info.view.*
import kotlinx.android.synthetic.main.fragment_info.view.toolbar
import org.koin.android.viewmodel.ext.android.viewModel

class InfoFragment : Fragment() {

    private lateinit var infoAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val model: InfoViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(fragment: View, savedInstanceState: Bundle?) {
        model.getDeviceInfo()
        (activity as AppCompatActivity).setSupportActionBar(fragment.toolbar)
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_keyboard_backspace_black_24dp)
        toolbar.setTitleTextColor(resources.getColor(R.color.logoTextColour))
        toolbar.title = resources.getString(R.string.info)
        toolbar.setNavigationOnClickListener {
            val navController = this.findNavController()
            navController.navigateUp()
        }
        viewManager = LinearLayoutManager(context) as RecyclerView.LayoutManager
        infoAdapter = InfoAdapter()
        (infoAdapter as InfoAdapter).saveNoteListener = {
            model.saveNote(it)
        }
        infoRv.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = infoAdapter
        }
        val dividerItemDecoration = DividerItemDecoration(
            fragment.infoRv.context,
            (viewManager as LinearLayoutManager).orientation
        )
        fragment.infoRv.addItemDecoration(dividerItemDecoration)
        viewLifecycleOwner.observe(model.deviceInfo) {
            (infoAdapter as InfoAdapter).updateInfo(it)
        }
    }
}
