package com.antonlarin.flexiblerecyclerview.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.antonlarin.flexiblerecyclerview.R
import com.antonlarin.flexiblerecyclerview.adapters.ItemDelegate
import com.antonlarin.flexiblerecyclerview.adapters.ItemsAdapter
import com.antonlarin.flexiblerecyclerview.model.Item
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ItemDelegate {

    lateinit var mainActivityViewModel: MainActivityViewModel

    lateinit var itemsObserver: Observer<Set<Item>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        val adapter = ItemsAdapter()
        adapter.attachDelegate(this)

        setupRecyclerView(adapter)

        itemsObserver = Observer {
            adapter.submitList(it.toList())
        }
    }

    override fun onStart() {
        super.onStart()
        mainActivityViewModel.items.observe(this, itemsObserver)
    }

    override fun onStop() {
        super.onStop()
        mainActivityViewModel.items.removeObserver(itemsObserver)
    }

    override fun removeItem(itemId: Int) {
        mainActivityViewModel.removeItem(itemId)
    }

    private fun setupRecyclerView(adapter: ItemsAdapter) {
        /* Устанавливаем адаптер и указываем необходимое кол-во столбцов в зависимости от конфигурации экрана (горизонтально/вертикально) */
        val columnNumber = resources.getInteger(R.integer.items_column)
        rvItemsList.layoutManager = GridLayoutManager(this, columnNumber)
        rvItemsList.adapter = adapter
    }

}