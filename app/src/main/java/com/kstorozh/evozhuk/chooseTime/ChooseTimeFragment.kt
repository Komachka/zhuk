package com.kstorozh.evozhuk.chooseTime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.kstorozh.evozhuk.R

class ChooseTimeFragment : Fragment() {

    var selectedButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_time_choose, container, false)
        val button: Button = view.findViewById(R.id.takeDevice)

        val buttonList = listOf<Button>(
            view.findViewById(R.id.oneHourBut),
            view.findViewById(R.id.twoHourBut),
            view.findViewById(R.id.fourHourBut),
            view.findViewById(R.id.allDayBut),
            view.findViewById(R.id.twoDaysBut),
            view.findViewById(R.id.anotherTimeBut)
        )

        buttonList.forEach {
            it.setOnClickListener {
                (it as Button).setBackgroundResource(R.drawable.time_but_pressed)
                it.setTextColor(getResources().getColor(R.color.but_time_focus))
                selectedButton?.let {
                    it.setBackgroundResource(R.drawable.round_rectangle)
                    it.setTextColor(getResources().getColor(R.color.but_time_def))
                }
                selectedButton = it
            }
        }

        button.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_chooseTimeFragment_to_backDeviceFragment)
        }
        return view
        }
}