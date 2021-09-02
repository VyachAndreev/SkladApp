package com.andreev.skladapp.ui._adapter

import android.util.ArraySet
import com.andreev.skladapp.ui._item.FilterItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.item_filter.view.*

class MultiSelectionAdapter : GroupAdapter<GroupieViewHolder>() {
    private var selectedItems = ArraySet<Int>()

    override fun onBindViewHolder(
        holder: GroupieViewHolder,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if (getItem(position) is FilterItem) {
            with(holder.itemView.checkbox) {
                isChecked = selectedItems.contains(position)
                setOnClickListener {
                    with(selectedItems) {
                        if (contains(position)) {
                            remove(position)
                        } else {
                            add(position)
                        }
                    }
                }
            }
        }
    }

    fun clearSelectedItems() {
        selectedItems.forEach {
            selectedItems.remove(it)
            notifyItemChanged(it)
        }
    }

    fun getSelectedNames(): List<String> {
        return arrayListOf<String>().apply {
            addAll(selectedItems.map { (getItem(it) as FilterItem).name })
        }
    }
}