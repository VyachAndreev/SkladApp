package com.andreev.skladapp.ui._adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.andreev.skladapp.R

class SelectionAdapter<T>(private val _context: Context, resource: Int, list: List<T>)
    : ArrayAdapter<T>(_context, resource, list)
{
    var selectedItem = 0

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v = super.getDropDownView(position, convertView, parent)
        if (position == selectedItem) {
            with(v.findViewById<TextView>(R.id.text_view)) {
                this.setBackgroundColor(ContextCompat.getColor(_context, R.color.blue_3B4))
                this.setTextColor(ContextCompat.getColor(_context, R.color.white))
            }
        }
        return v
    }
}