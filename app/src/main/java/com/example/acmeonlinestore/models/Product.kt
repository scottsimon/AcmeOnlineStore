package com.example.acmeonlinestore.models

import kotlinx.serialization.Serializable

@Serializable
data class Product(
  val id: String,
  val name: String,
  val description: String,
  val fullDescription: String,
  val price: Double,
  val seller: String,
  val thumbnail: String
)

@Serializable
data class Products(
  val products: List<Product>
)
