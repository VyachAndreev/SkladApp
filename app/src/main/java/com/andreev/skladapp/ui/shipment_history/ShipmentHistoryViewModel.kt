package com.andreev.skladapp.ui.shipment_history

import androidx.lifecycle.MutableLiveData
import com.andreev.skladapp.data.TablePiece
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._base.BaseViewModel

class ShipmentHistoryViewModel: BaseViewModel() {

    val historyPieces = MutableLiveData<Array<TablePiece>>()

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    fun getHistory() {
    }
}