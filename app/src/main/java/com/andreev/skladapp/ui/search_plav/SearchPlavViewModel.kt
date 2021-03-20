package com.andreev.skladapp.ui.search_plav

import com.andreev.skladapp.data.Position
import com.andreev.skladapp.ui.search.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchPlavViewModel: SearchViewModel() {

    override suspend fun loadPositions(): Array<Position>? {
        return itemsRepository.getPlavPositions(lastSearched)
    }

    override fun getHints(tag: String?) {
        scopeMain.launch {
            val response = withContext(Dispatchers.IO) {
                itemsRepository.getPlavHints(tag)
            }
            if (response != null) {
                hints.value = response
            }
        }
    }
}