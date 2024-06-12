package com.example.storeslist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storeslist.data.models.Store
import com.example.storeslist.databinding.ItemStoreListBinding

class StoreAdapter(private var stores: List<Store>) : RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {

    inner class StoreViewHolder(val binding: ItemStoreListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val binding = ItemStoreListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val store = stores[position]
        holder.binding.tvName.text = store.name
        holder.binding.tvCode.text = store.code
        holder.binding.tvAddress.text = store.address
    }

    override fun getItemCount(): Int = stores.size

    fun updateStores(newStoreList: List<Store>) {
        val diffCallback = StoreDiffCallback(stores, newStoreList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        stores = newStoreList
        diffResult.dispatchUpdatesTo(this)
    }
}