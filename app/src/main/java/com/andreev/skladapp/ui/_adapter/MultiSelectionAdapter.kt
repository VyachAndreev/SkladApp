package com.andreev.skladapp.ui._adapter

import com.andreev.skladapp.ui._item.FilterItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.item_filter.view.*

class MultiSelectionAdapter : GroupAdapter<GroupieViewHolder>() {
    private var selectedItems = HashSet<Int>()

    override fun onBindViewHolder(
        holder: GroupieViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if (getItem(position) is FilterItem) {
            holder.itemView.checkbox.isChecked = selectedItems.contains(position)
            holder.itemView.checkbox.setOnClickListener {
                if (selectedItems.contains(position)) {
                    selectedItems.remove(position)
                } else {
                    selectedItems.add(position)
                }
            }
        }
    }

    fun clearSelectedItems() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

    fun getSelectedNames(): ArrayList<String> {
        val names = arrayListOf<String>()
        names.addAll(
            selectedItems.map {
                (getItem(it) as FilterItem).name
            }
        )
        return names
    }
}