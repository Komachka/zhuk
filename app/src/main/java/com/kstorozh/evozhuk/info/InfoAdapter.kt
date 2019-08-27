package com.kstorozh.evozhuk.info

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.kstorozh.evozhuk.*
import kotlinx.android.synthetic.main.info_item.view.*
import kotlinx.android.synthetic.main.note_item.view.*

class InfoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val infoAboutDevice = ArrayList<Pair<DeviceInfoName, DeviceInfoParam>>()

    fun updateInfo(info: List<Pair<DeviceInfoName, DeviceInfoParam>>) {
        infoAboutDevice.clear()
        infoAboutDevice.addAll(info)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when (viewType) {
            R.layout.note_item -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
                NoteViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.info_item, parent, false)
                InfoViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return infoAboutDevice.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            getItemViewType(position) == R.layout.note_item -> (holder as NoteViewHolder).bindData(infoAboutDevice[position])
            else -> (holder as InfoViewHolder).bindData(infoAboutDevice[position])
        }
    }

    class InfoViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(dataPair: Pair<DeviceInfoName, DeviceInfoParam>) {
            val enumInfoName = FieldsInfo.valueOf(dataPair.first)
            view.categoryNameTv.text = view.context.resources.getString(enumInfoName.resursId)
            view.categoryValueTv.text = dataPair.second
        }
    }

    lateinit var saveNoteListener: (String) -> Unit

    inner class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        override fun onClick(v: View?) {
            v?.let {
                if (v.id == R.id.submitNoteBtn) {
                    val text = view.newNoteEt.text.toString()
                    if (view.noteMessageTv.text != text) {
                        saveNoteListener.invoke(text)
                        view.noteMessageTv.text = text
                    }
                    if (text.isNotEmpty()) {
                        view.noteMessageTv.visibility = View.VISIBLE
                        view.newNoteEt.visibility = View.GONE
                        view.submitNoteBtn.visibility = View.GONE
                    }
                }
                if (v.id == R.id.noteMessageTv) {
                    view.noteMessageTv.visibility = View.GONE
                    view.newNoteEt.visibility = View.VISIBLE
                    view.submitNoteBtn.visibility = View.VISIBLE
                }
            }
        }

        fun bindData(dataPair: Pair<DeviceInfoName, DeviceInfoParam>) {
            val enumInfoName = FieldsInfo.valueOf(dataPair.first)
            Log.d(LOG_TAG, enumInfoName.resursId.toString())
            view.noteTitleTv.text = view.context.resources.getString(enumInfoName.resursId)
            view.noteMessageTv.text = dataPair.second
            view.newNoteEt.setText(dataPair.second)
            view.newNoteEt.visibility = View.GONE
            view.submitNoteBtn.visibility = View.GONE

            view.noteMessageTv.setOnClickListener(this)
            view.submitNoteBtn.setOnClickListener(this)

            if (dataPair.second.isEmpty()) {
                view.noteMessageTv.visibility = View.GONE
                view.newNoteEt.visibility = View.VISIBLE
                view.submitNoteBtn.visibility = View.VISIBLE
            } else {
                view.noteMessageTv.visibility = View.VISIBLE
                view.newNoteEt.visibility = View.GONE
                view.submitNoteBtn.visibility = View.GONE
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val infoPair = infoAboutDevice[position]
        return if (infoPair.first == INFO_NOTE) {
            R.layout.note_item
        } else {
            R.layout.info_item
        }
    }
}
