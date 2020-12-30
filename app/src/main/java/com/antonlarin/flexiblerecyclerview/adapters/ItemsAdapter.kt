package com.antonlarin.flexiblerecyclerview.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.antonlarin.flexiblerecyclerview.R
import com.antonlarin.flexiblerecyclerview.model.Item

interface ItemDelegate{
    /* Интерфейс для отработки действий с элементом */
    fun removeItem(itemId: Int)
}

class ItemsAdapter : RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>() {

    private var items: List<Item> = listOf()
    private var itemDelegate : ItemDelegate? = null

    override fun getItemCount(): Int = items.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        return ItemsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false), itemDelegate)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.item = items[position]
    }

    fun attachDelegate(itemDelegate: ItemDelegate){
        this.itemDelegate = itemDelegate
    }

    fun submitList(items: List<Item>){
        /* Использую DiffUtils для более бысрой работы + анимация при добавлении/удалении */
        val diffResult : DiffUtil.DiffResult = DiffUtil.calculateDiff(ItemsDiffCallback(this.items, items))
        this.items = items
        diffResult.dispatchUpdatesTo(this)
    }

    class ItemsDiffCallback(var oldList: List<Item>, var newList: List<Item>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].id == newList[newItemPosition].id)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    class ItemsViewHolder(view: View, private val itemDelegate: ItemDelegate?) : RecyclerView.ViewHolder(view){
        /* Установка onClickListener и присваивание каждому элементу его номер */
        var item : Item? = null
            set(value){
                value?.let {newValue ->
                    field = newValue
                    itemView.findViewById<Button>(R.id.btnRemoveItem).setOnClickListener { item?.id?.let { item -> itemDelegate?.removeItem(item) } }
                    itemView.findViewById<TextView>(R.id.tvNumber).text = newValue.id.toString()
                }
            }
    }
}