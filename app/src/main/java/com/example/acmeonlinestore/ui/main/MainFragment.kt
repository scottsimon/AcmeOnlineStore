package com.example.acmeonlinestore.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.acmeonlinestore.databinding.MainFragmentBinding
import com.example.acmeonlinestore.models.Product
import com.example.acmeonlinestore.ui.RecyclerViewAdapter
import com.example.acmeonlinestore.BR
import com.example.acmeonlinestore.R

class MainFragment : Fragment() {

  companion object {
    fun newInstance() = MainFragment()
  }

  private val viewModel: MainViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val binding = MainFragmentBinding.inflate(inflater, container, false)
    binding.viewModel = viewModel
    binding.lifecycleOwner = this

    val adapter = ProductsAdapter()
    binding.productsRecyclerview.adapter = adapter

    viewModel.products.observe(viewLifecycleOwner) {
      val items = it.map { product ->
        ProductItem(lifecycleScope, requireContext(), product)
      }
      adapter.submitList(items)
    }

    return binding.root
  }

  private inner class ProductsAdapter: RecyclerViewAdapter<ProductItem>(this, BR.item) {
    override fun getItemViewType(position: Int): Int {
      return R.layout.product_summary
    }
  }

}
