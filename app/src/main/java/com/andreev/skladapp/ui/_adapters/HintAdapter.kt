package com.andreev.skladapp.ui._adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.andreev.skladapp.R
import timber.log.Timber

class HintAdapter(context: Context, val stringList: ArrayList<String>, val listener: ClickListener)
    : ArrayAdapter<String>(context, R.layout.item_search_hint, stringList) {

    interface ClickListener {
        fun onClick()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        convertView?.setOnClickListener {
            listener.onClick()
            Timber.i("Clicked")
        }
        return super.getView(position, convertView, parent)

    }
}