package com.example.storeslist.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.storeslist.data.models.Store

class StoreDiffCallback(private val oldList: List<Store>, private val newList: List<Store>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].code == newList[newItemPosition].code
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}