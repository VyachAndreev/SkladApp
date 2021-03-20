package com.andreev.skladapp.ui._adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.andreev.skladapp.R

class SelectionAdapter<T>(private val cntxt: Context, resource: Int, arrayList: List<T>)
    : ArrayAdapter<T>(cntxt, resource, arrayList) {

    var selectedItem = 0

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v = super.getDropDownView(position, convertView, parent)
        if (position == selectedItem) {
            val textView = v.findViewById<TextView>(R.id.text_view)
            textView.setBackgroundColor(cntxt.resources.getColor(R.color.blue_3B4))
            textView.setTextColor(cntxt.resources.getColor(R.color.white))
        }
        return v
    }
}