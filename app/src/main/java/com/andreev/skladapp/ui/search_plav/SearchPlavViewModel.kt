package com.andreev.skladapp.ui.search_plav

import com.andreev.skladapp.data.Position
import com.andreev.skladapp.ui.search.SearchViewModel

class SearchPlavViewModel: SearchViewModel() {

    override suspend fun loadPositions(): Array<Position>? {
        return itemsRepository.getPlavPositions(lastSearched)
    }
}