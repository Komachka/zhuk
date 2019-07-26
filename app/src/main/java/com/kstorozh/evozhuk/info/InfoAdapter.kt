package com.kstorozh.evozhuk.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.kstorozh.evozhuk.R

class InfoAdapter(private val infoAboutDevice: List<Pair<String, String>>) : RecyclerView.Adapter<InfoAdapter.InfoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.info_item, parent, false)
        return InfoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return infoAboutDevice.size
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        holder.bindData(infoAboutDevice[position])
    }

    class InfoViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val categoryNameTv: TextView = view.findViewById(R.id.categoryNameTv)
        private val categoryValueTv: TextView = view.findViewById(R.id.categoryValueTv)

        fun bindData(dataPair: Pair<String, String>) {

            val enumInstance = FieldsInfo.valueOf(dataPair.first)
            categoryNameTv.text = view.context.resources.getString(enumInstance.resurs)
            categoryValueTv.text = dataPair.second
        }
    }
}
