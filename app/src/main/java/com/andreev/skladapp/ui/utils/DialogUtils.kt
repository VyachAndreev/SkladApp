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
import com.andreev.skladapp.data.HistoryPiece
import com.andreev.skladapp.databinding.DialogHistoryBinding
import com.andreev.skladapp.databinding.ItemTextViewBinding
import com.andreev.skladapp.ui._item.HistoryItem
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
}