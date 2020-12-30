package com.antonlarin.flexiblerecyclerview.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.antonlarin.flexiblerecyclerview.interfaces.RepositoryInterface
import com.antonlarin.flexiblerecyclerview.model.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt

const val DELAY_TIME = 5_000L

/*
*  Репозиторий, в котором каждые 5 секунд обновляется LiveData содержащая Set из Item (изначально 15 элементов)
*  При удалении элемента он удаляется из LiveData и записывается в пул удалленных элементов, из которого впоследствии добавляется в LiveData в порядке очереди
*  При добавлении элемента (из пула или при наращивании номера) позиция выбирается случайным образом
*/

class Repository : RepositoryInterface {

    private val _items: MutableLiveData<Set<Item>> = MutableLiveData<Set<Item>>()

    private val removedItems: MutableSet<Item> = mutableSetOf()

    init {
        firstInit()
        setUpdatingData()
    }

    override fun removeItem(itemId: Int) {
        /* Удаление элемента из LiveData и добавление в пул удаленных элементов */
        val currentItems = _items.value?.toMutableList() ?: mutableListOf()
        val itemToRemove = _items.value?.firstOrNull {
            it.id == itemId
        }
        currentItems.remove(itemToRemove)
        _items.value = currentItems.toSet()

        itemToRemove?.let { removedItems.add(it) }
    }

    override fun getItems(): LiveData<Set<Item>> = _items

    private fun setUpdatingData() {
        /* Каждые 5 секунд добавляет элемент в LiveData (Наращивает номер или берет из пула удаленных элементов) */
        CoroutineScope(IO).launch {
            while (true) {
                delay(DELAY_TIME)

                val currentItems = _items.value?.toMutableList() ?: mutableListOf()
                val randomIndex = Random.nextInt(0..currentItems.size)

                if (removedItems.isNotEmpty()) {
                    val removedItem = removedItems.first()
                    currentItems.add(randomIndex, removedItem)
                    removedItems.remove(removedItem)
                } else {
                    val newItem = Item(currentItems.size)
                    currentItems.add(randomIndex, newItem)
                }
                _items.postValue(currentItems.toSet())
            }
        }
    }

    private fun firstInit() {
        /* Инициализируем LiveData 15-ю начальными элементами */
        val initialItems = mutableSetOf<Item>()
        for (id in 0 until 15) {
            initialItems.add(Item(id))
        }
        _items.value = initialItems
    }

}