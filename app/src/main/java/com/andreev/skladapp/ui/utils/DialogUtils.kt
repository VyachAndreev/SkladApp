package com.andreev.skladapp.ui.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StyleRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreev.skladapp.R
import com.andreev.skladapp.data.HistoryPiece
import com.andreev.skladapp.databinding.DialogFilterBinding
import com.andreev.skladapp.databinding.DialogHistoryBinding
import com.andreev.skladapp.databinding.ItemTextViewBinding
import com.andreev.skladapp.ui._item.FilterItem
import com.andreev.skladapp.ui._item.HistoryItem
import com.andreev.skladapp.ui._item.TextViewItem
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.tabs.TabLayout
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

object DialogUtils {

    private fun baseDialog(view: View, @StyleRes dialogStyle: Int? = null): AlertDialog {
        val dialog: AlertDialog = if (dialogStyle != null) {
            AlertDialog.Builder(view.context, dialogStyle)
                .setView(view)
                .create()
        } else {
            AlertDialog.Builder(view.context)
                .setView(view)
                .create()
        }
        return dialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    fun showHistoryDialog(
        context: Context?,
        historyPieces: ArrayList<HistoryPiece>,
    ) {
        lateinit var dialog: AlertDialog
        val dialogBinding = DataBindingUtil.inflate<DialogHistoryBinding>(
            LayoutInflater.from(context),
            R.layout.dialog_history,
            null,
            false
        )
        var recyclerAdapter = GroupAdapter<GroupieViewHolder>()
        dialogBinding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        if (historyPieces.size > 0) {
            recyclerAdapter.addAll(historyPieces.map {
                HistoryItem(it)
            })
        } else {
            recyclerAdapter.add(TextViewItem("Данные отсутствуют"))
        }

        dialog =
            baseDialog(dialogBinding.root)

        dialog.show()
    }

    fun showFilterDialog(
        context: Context?,
        marks: Array<String>?,
        diameters: Array<String>?,
        packings: Array<String>?,
        onFilterPressed: (Array<Array<String>>) -> (Unit),
    ) {
        lateinit var dialog: AlertDialog
        val dialogBinding = DataBindingUtil.inflate<DialogFilterBinding>(
            LayoutInflater.from(context),
            R.layout.dialog_filter,
            null,
            false
        )
        val markAdapter = GroupAdapter<GroupieViewHolder>()
        val packingAdapter = GroupAdapter<GroupieViewHolder>()
        dialogBinding.apply {
            markRecycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = markAdapter
            }
            var valueFrom = diameters?.get(0)?.toFloat()
            var valueTo = diameters?.get(1)?.toFloat()
            fromEt.setText(valueFrom.toString().substring(
                0, valueFrom.toString().indexOf(".") + 2
            ))
            toEt.setText(valueTo.toString().substring(
                0, valueFrom.toString().indexOf(".") + 2
            ))
            rangeSlider.valueTo = Float.MAX_VALUE
            rangeSlider.valueFrom = Float.MIN_VALUE
            if (valueTo != null) {
                rangeSlider.valueTo = valueTo
            }
            if (valueFrom != null) {
                rangeSlider.valueFrom = valueFrom
            }
            rangeSlider.setValues(valueFrom, valueTo)
            rangeSlider.stepSize = 0.1f
            rangeSlider.addOnChangeListener { slider, value, fromUser ->
                valueFrom = rangeSlider.values[0]
                valueTo = rangeSlider.values[1]
                fromEt.setText(valueFrom.toString().substring(
                    0, valueFrom.toString().indexOf(".") + 3
                ))
                toEt.setText(valueTo.toString().substring(
                    0, valueFrom.toString().indexOf(".") + 3
                ))
            }
            packingRecycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = packingAdapter
            }

            filterBtn.setOnClickListener {
                dialog.dismiss()
            }
        }

        marks?.map {
            FilterItem(it)
        }?.let {
            markAdapter.addAll(
                it
            )
        }
        packings?.map {
            FilterItem(it)
        }?.let {
            markAdapter.addAll(
                it
            )
        }

        dialog =
            baseDialog(dialogBinding.root)

        dialog.show()

    }
}