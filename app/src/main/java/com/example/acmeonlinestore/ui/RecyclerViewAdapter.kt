package com.example.acmeonlinestore.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import java.security.InvalidParameterException

abstract class RecyclerViewItem(val coroutineScope: CoroutineScope, val context: Context) {
  open fun onBind() { }
  open fun onViewRecycled() { }
}

class ItemViewHolder<T>(
  private val binding: ViewDataBinding,
  private val lifecycleOwner: LifecycleOwner
) : RecyclerView.ViewHolder(binding.root) {
  fun bind(item: T, dataBindingID: Int) {
    if (!binding.setVariable(dataBindingID, item)) {
      throw InvalidParameterException("dataBindingID was not found in the layout")
    }
    binding.lifecycleOwner = lifecycleOwner
    binding.executePendingBindings()
  }
}

abstract class RecyclerViewAdapter<T>(
  private val lifecycleOwner: LifecycleOwner,
  private val dataBindingID: Int,
  diffCallback: DiffUtil.ItemCallback<T> = RecyclerViewItemDiffCallback()
) : ListAdapter<T, ItemViewHolder<T>>(diffCallback) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder<T> {
    val layoutInflater = LayoutInflater.from(parent.context)
    val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
    return ItemViewHolder(binding, lifecycleOwner)
  }

  override fun onBindViewHolder(holder: ItemViewHolder<T>, position: Int) {
    val item = getItem(position)
    (item as? RecyclerViewItem)?.onBind()
    holder.bind(item, dataBindingID)
  }

  abstract override fun getItemViewType(position: Int): Int

  override fun onViewRecycled(holder: ItemViewHolder<T>) {
    if (holder.bindingAdapterPosition >= 0) {
      val item = getItem(holder.bindingAdapterPosition)
      (item as? RecyclerViewItem)?.onViewRecycled()
    }
    super.onViewRecycled(holder)
  }
}

class RecyclerViewItemDiffCallback<T> : DiffUtil.ItemCallback<T>() {
  override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
    return oldItem == newItem
  }

  @SuppressLint("DiffUtilEquals")
  override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
    return oldItem == newItem
  }
}
