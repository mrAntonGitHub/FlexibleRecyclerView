package com.antonlarin.flexiblerecyclerview.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.antonlarin.flexiblerecyclerview.interfaces.RepositoryInterface
import com.antonlarin.flexiblerecyclerview.model.Item
import com.antonlarin.flexiblerecyclerview.repository.Repository

/*
*  ViewModel, которая хранит данные View и взаимодействует с Repository
*/

class MainActivityViewModel : ViewModel() {

    private val repository: RepositoryInterface = Repository()

    val items: LiveData<Set<Item>> = repository.getItems()

    fun removeItem(itemId: Int) {
        repository.removeItem(itemId)
    }

}