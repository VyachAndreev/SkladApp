package com.andreev.skladapp.ui.shipment_history

import androidx.lifecycle.MutableLiveData
import com.andreev.skladapp.data.HistoryPiece
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._base.BaseViewModel

class ShipmentHistoryViewModel: BaseViewModel() {

    val historyPieces = MutableLiveData<Array<HistoryPiece>>()

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    fun getHistory() {
    }
}