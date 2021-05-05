package com.andreev.skladapp.ui.search_plav

import com.andreev.skladapp.data.Position
import com.andreev.skladapp.stored_data.UserStoredData
import com.andreev.skladapp.ui.search.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchPlavViewModel: SearchViewModel() {

    override suspend fun loadPositions(): Array<Position>? {
        return userStoredData.user?.let { itemsRepository.getPlavPositions(lastSearched, it) }
    }

    override fun getHints(tag: String?) {
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                userStoredData.user?.let { itemsRepository.getPlavHints(tag, it) }
            }
            if (response != null) {
                hints.value = response
            }
        }
    }
}