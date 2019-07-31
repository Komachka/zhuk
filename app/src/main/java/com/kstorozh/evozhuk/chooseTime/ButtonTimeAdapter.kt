package com.kstorozh.evozhuk.chooseTime

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.kstorozh.evozhuk.R

class ButtonTimeAdapter(private val timeButtonList: List<TimeButton>) : RecyclerView.Adapter<ButtonTimeAdapter.ViewHolder>() {

    var selectedIndex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val button = LayoutInflater.from(parent.context).inflate(R.layout.item_button, parent, false) as Button
        return ViewHolder(button)
    }

    override fun getItemCount(): Int {
        return timeButtonList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(timeButtonList[position])
        holder.buttonItem.setOnClickListener {
            timeButtonList[selectedIndex].isSelected = false
            timeButtonList[position].isSelected = true
            selectedIndex = position
            timeButtonList[position].navigation?.let {
                it.invoke()
            }
            notifyDataSetChanged()
        }
    }

    class ViewHolder(val buttonItem: Button) : RecyclerView.ViewHolder(buttonItem) {
        fun bind(timeButton: TimeButton) {
            buttonItem.text = timeButton.text
            if (timeButton.isSelected) {
                buttonItem.setBackgroundResource(R.drawable.time_but_pressed)
                buttonItem.setTextColor(buttonItem.context.resources.getColor(R.color.but_time_focus))
            } else {
                buttonItem.setBackgroundResource(R.drawable.round_rectangle)
                buttonItem.setTextColor(buttonItem.context.resources.getColor(R.color.but_time_def))
            }
        }
    }
}

class TimeButton(
    val text: String,
    val milisec: Long,
    var isSelected: Boolean = false,
    val navigation: (() -> Unit)? = null
)
