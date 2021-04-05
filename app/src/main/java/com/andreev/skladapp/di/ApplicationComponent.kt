package com.andreev.skladapp.di

import com.andreev.skladapp.di.modules.RepositoryModule
import com.andreev.skladapp.ui.MainActivity
import com.andreev.skladapp.di.modules.SettingsModule
import com.andreev.skladapp.ui.get.GetViewModel
import com.andreev.skladapp.ui.hub.HubFragment
import com.andreev.skladapp.ui.hub.HubViewModel
import com.andreev.skladapp.ui.information.InformationViewModel
import com.andreev.skladapp.ui.search.SearchViewModel
import com.andreev.skladapp.ui.shipment.ShipmentViewModel
import com.andreev.skladapp.ui.shipment_history.ShipmentHistoryViewModel
import com.andreev.skladapp.ui.show_all.ShowAllViewModel
import com.andreev.skladapp.ui.sign_in.SignInViewModel
import com.andreev.skladapp.ui.unite.UniteViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [SettingsModule::class, RepositoryModule::class])
@Singleton
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(hubViewModel: HubViewModel)
    fun inject(hubFragment: HubFragment)

    fun inject(signInViewModel: SignInViewModel)

    fun inject(searchViewModel: SearchViewModel)

    fun inject(informationViewModel: InformationViewModel)

    fun inject(showAllViewModel: ShowAllViewModel)

    fun inject(uniteViewModel: UniteViewModel)

    fun inject(getViewModel: GetViewModel)

    fun inject(shipmentViewModel: ShipmentViewModel)

    fun inject(shipmentHistoryViewModel: ShipmentHistoryViewModel)
}