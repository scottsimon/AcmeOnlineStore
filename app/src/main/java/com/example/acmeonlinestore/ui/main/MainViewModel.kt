package com.example.acmeonlinestore.ui.main

import android.app.Application
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.acmeonlinestore.R
import com.example.acmeonlinestore.models.Product
import com.example.acmeonlinestore.models.Products
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringWriter
import java.io.Writer

@Serializable
data class Foo(val name: String)

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
    val json = loadProductsJson(getApplication<Application>().resources)
    return Json { ignoreUnknownKeys = true }.decodeFromString<Products>(json)
  }

  private fun loadProductsJson(resources: Resources): String {
    return loadRawJson(resources, R.raw.products)
  }

  private fun loadRawJson(resources: Resources, id: Int): String {
    val stream: InputStream = resources.openRawResource(id)
    val writer: Writer = StringWriter()
    val buffer = CharArray(1024)
    try {
      val reader: Reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
      var n: Int
      while (reader.read(buffer).also { n = it } != -1) {
        writer.write(buffer, 0, n)
      }
    } catch (t: Throwable) {
      Log.e("***", "loadRawJson: error!", t)
    }

    try {
      stream.close()
    } catch (t: Throwable) {
      Log.e("***", "loadRawJson: error closing input stream", t)
    }

    return writer.toString()
  }
}

