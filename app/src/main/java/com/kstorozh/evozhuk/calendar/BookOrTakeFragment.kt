package com.kstorozh.evozhuk.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.R
import kotlinx.android.synthetic.main.fragment_book_or_take.view.*

class BookOrTakeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_or_take, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.takeBut.setOnClickListener {
            arguments?.let {
                with(BookOrTakeFragmentDirections.actionBookOrTakeFragmentToChooseTimeFragment(BookOrTakeFragmentArgs.fromBundle(it).userId)) {
                    Navigation.findNavController(view).navigate(this)
                }
            }
        }
        view.bookBut.setOnClickListener {
            arguments?.let {
                with(BookOrTakeFragmentDirections.actionBookOrTakeFragmentToCalendarFragment(BookOrTakeFragmentArgs.fromBundle(it).userId)) {
                    Navigation.findNavController(view).navigate(this)
                }
            }
        }
    }
}
