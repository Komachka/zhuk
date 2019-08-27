package com.kstorozh.evozhuk.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.kstorozh.evozhuk.DeviceInfoName
import com.kstorozh.evozhuk.DeviceInfoParam
import com.kstorozh.evozhuk.R
import kotlinx.android.synthetic.main.info_item.view.*

class InfoAdapter : RecyclerView.Adapter<InfoAdapter.InfoViewHolder>() {

    private val infoAboutDevice = ArrayList<Pair<DeviceInfoName, DeviceInfoParam>>()

    fun updateInfo(info: List<Pair<DeviceInfoName, DeviceInfoParam>>)
    {
        infoAboutDevice.clear()
        infoAboutDevice.addAll(info)
        notifyDataSetChanged()
    }

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
            val enumInfoName = FieldsInfo.valueOf(dataPair.first)
            view.categoryNameTv.text = view.context.resources.getString(enumInfoName.resursId)
            view.categoryValueTv.text = dataPair.second
        }
    }
}
