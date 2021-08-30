package com.andreev.skladapp.ui.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StyleRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreev.skladapp.R
import com.andreev.skladapp.data.TablePiece
import com.andreev.skladapp.databinding.DialogFilterBinding
import com.andreev.skladapp.databinding.DialogHistoryBinding
import com.andreev.skladapp.databinding.DialogShipmentBinding
import com.andreev.skladapp.network.repositories.ItemsRepository
import com.andreev.skladapp.ui._adapter.MultiSelectionAdapter
import com.andreev.skladapp.ui._item.FilterItem
import com.andreev.skladapp.ui._item.TableItem
import com.andreev.skladapp.ui._item.TextViewItem
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

//    fun showHistoryDialog(
//        context: Context?,
//        tablePieces: ArrayList<TablePiece>,
//    ) {
//        lateinit var dialog: AlertDialog
//        val dialogBinding = DataBindingUtil.inflate<DialogHistoryBinding>(
//            LayoutInflater.from(context),
//            R.layout.dialog_history,
//            null,
//            false
//        )
//        var recyclerAdapter = GroupAdapter<GroupieViewHolder>()
//        dialogBinding.recycler.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = recyclerAdapter
//        }
//
//        if (tablePieces.size > 0) {
//            recyclerAdapter.addAll(tablePieces.map {
//                TableItem(it)
//            })
//        } else {
//            recyclerAdapter.add(TextViewItem("Данные отсутствуют"))
//        }
//
//        dialog =
//            baseDialog(dialogBinding.root)
//
//        dialog.show()
//    }

    fun showFilterDialog(
        context: Context?,
        marks: Array<String>?,
        diameters: Array<String>?,
        packings: Array<String>?,
        onFilterPressed: (ItemsRepository.FilterClass) -> (Unit),
    ) {
        lateinit var dialog: AlertDialog
        val dialogBinding = DataBindingUtil.inflate<DialogFilterBinding>(
            LayoutInflater.from(context),
            R.layout.dialog_filter,
            null,
            false
        )
        val markAdapter = MultiSelectionAdapter()
        markAdapter.setHasStableIds(true)
        val packingAdapter = MultiSelectionAdapter()
        packingAdapter.setHasStableIds(true)
        dialogBinding.apply {
            markRecycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = markAdapter
            }
            var valueFrom = diameters?.get(0)?.toFloat()
            var valueTo = diameters?.get(1)?.toFloat()
            fromEt.text = valueFrom.toString().substring(
                0, valueFrom.toString().indexOf(".") + 2
            )
            toEt.text = valueTo.toString().substring(
                0, valueFrom.toString().indexOf(".") + 2
            )
            if (valueTo == valueFrom) {
                rangeSlider.visibility = View.GONE
                fromEt.text = valueFrom.toString().substring(
                    0, valueFrom.toString().indexOf(".") + 2
                )
                toEt.text = valueTo.toString().substring(
                    0, valueFrom.toString().indexOf(".") + 2
                )
            } else {
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
                rangeSlider.addOnChangeListener { _, _, _ ->
                    valueFrom = rangeSlider.values[0]
                    valueTo = rangeSlider.values[1]
                    fromEt.text = valueFrom.toString().substring(
                        0, valueFrom.toString().indexOf(".") + 2
                    )
                    toEt.text = valueTo.toString().substring(
                        0, valueFrom.toString().indexOf(".") + 2
                    )
                }
            }
            packingRecycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = packingAdapter
            }


            filterBtn.setOnClickListener {
                onFilterPressed(
                    ItemsRepository.FilterClass(
                        markAdapter.getSelectedNames(),
                        packingAdapter.getSelectedNames(),
                        fromEt.text.toString().toDouble(),
                        toEt.text.toString().toDouble(),
                    )
                )
                dialog.dismiss()
            }

            clearFilter.setOnClickListener {
                markAdapter.clearSelectedItems()
                packingAdapter.clearSelectedItems()
                rangeSlider.setValues(valueFrom, valueTo)
            }
        }

        marks?.map { FilterItem(it) }?.let { markAdapter.addAll(it) }
        packings?.map { FilterItem(it) }?.let { packingAdapter.addAll(it) }

        dialog = baseDialog(dialogBinding.root)

        dialog.show()

    }

    fun showShipDialog(
        context: Context?,
        weight: String?,
        onShip: (Triple<String, String, String>) -> (Unit),
    ) {
        lateinit var dialog: AlertDialog
        val dialogBinding = DataBindingUtil.inflate<DialogShipmentBinding>(
            LayoutInflater.from(context),
            R.layout.dialog_shipment,
            null,
            false
        )
        dialogBinding.apply {
            if (weight != null)
                etWeight.setText(weight.toString())
            btnShip.setOnClickListener {
                onShip(
                    Triple(
                        etWeight.text.toString(),
                        agentEt.text.toString(),
                        checkEt.text.toString(),
                    )
                )
                dialog.dismiss()
            }
        }

        dialog =
            baseDialog(dialogBinding.root)

        dialog.show()
    }
}