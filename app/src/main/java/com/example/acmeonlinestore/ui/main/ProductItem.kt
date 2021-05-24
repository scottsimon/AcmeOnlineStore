package com.example.acmeonlinestore.ui.main

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.acmeonlinestore.R
import com.example.acmeonlinestore.models.Product
import com.example.acmeonlinestore.ui.RecyclerViewItem
import kotlinx.coroutines.CoroutineScope

class ProductItem(
  coroutineScope: CoroutineScope,
  context: Context,
  val product: Product
): RecyclerViewItem(coroutineScope, context) {

  val title = product.name

  val description = product.description

  val price: String = String.format(context.getString(R.string.product_price_format), product.price)

  private val _thumbnail = MutableLiveData<Drawable>()
  val thumbnail: LiveData<Drawable> = _thumbnail

  override fun onBind() {
    val url = product.thumbnail
    Glide.with(context)
      .load(url)
      .into(object : CustomTarget<Drawable>() {
        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
          _thumbnail.postValue(resource)
        }

        override fun onLoadCleared(placeholder: Drawable?) {
          Log.d("***", "onLoadCleared: ")
        }
      })
  }
}
