package com.kstorozh.evozhuk.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.getInfoPairs
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_info.view.*

class InfoFragment : Fragment() {

    private lateinit var infoRecView: RecyclerView
    private lateinit var infoAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragment = inflater.inflate(R.layout.fragment_info, container, false)
        (activity as AppCompatActivity).setSupportActionBar(fragment.toolbar)
        fragment.toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_keyboard_backspace_black_24dp)
        fragment.toolbar.title = resources.getString(R.string.info)

        fragment.toolbar.setNavigationOnClickListener {
            val navController = this.findNavController()
            navController.navigateUp()
        }

        viewManager = LinearLayoutManager(context)
        infoAdapter = InfoAdapter(context.getInfoPairs())
        fragment.infoRv.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = infoAdapter
        }

        val dividerItemDecoration = DividerItemDecoration(
            fragment.infoRv.context,
            (viewManager as LinearLayoutManager).orientation
        )
        fragment.infoRv.addItemDecoration(dividerItemDecoration)

        return fragment
    }
}
