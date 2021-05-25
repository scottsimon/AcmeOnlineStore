package com.example.acmeonlinestore.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.acmeonlinestore.R
import com.example.acmeonlinestore.extensions.readAsString
import com.example.acmeonlinestore.models.Product
import com.example.acmeonlinestore.models.Products
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class MainViewModel(application: Application) : AndroidViewModel(application) {

  private val _products = MutableLiveData<List<Product>>()
  val products: LiveData<List<Product>> = _products

  init {
    viewModelScope.launch {
      val products = loadProducts()
      _products.postValue(products.products)
    }
  }

  private fun loadProducts(): Products {
    val json = getApplication<Application>().resources
      .openRawResource(R.raw.products)
      .readAsString()
      .orEmpty()
    return Json { ignoreUnknownKeys = true }.decodeFromString<Products>(json)
  }
}

