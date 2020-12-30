package com.antonlarin.flexiblerecyclerview.interfaces

import androidx.lifecycle.LiveData
import com.antonlarin.flexiblerecyclerview.model.Item

interface RepositoryInterface {

    fun removeItem(itemId: Int)
    fun getItems(): LiveData<Set<Item>>

}