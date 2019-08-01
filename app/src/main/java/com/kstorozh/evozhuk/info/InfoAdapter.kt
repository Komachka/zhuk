package com.kstorozh.evozhuk.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.kstorozh.evozhuk.DeviceInfoName
import com.kstorozh.evozhuk.DeviceInfoParam
import com.kstorozh.evozhuk.R
import kotlinx.android.synthetic.main.info_item.view.*

class InfoAdapter(private val infoAboutDevice: List<Pair<DeviceInfoName, DeviceInfoParam>>) : RecyclerView.Adapter<InfoAdapter.InfoViewHolder>() {
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
        fun bindData(dataPair: Pair<DeviceInfoName, DeviceInfoParam>) {

            val enumInstance = FieldsInfo.valueOf(dataPair.first)
            view.categoryNameTv.text = view.context.resources.getString(enumInstance.resurs)
            view.categoryValueTv.text = dataPair.second
        }
    }
}
