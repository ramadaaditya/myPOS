package com.example.findest.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProductResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("products")
	val products: List<ProductsItem?>? = null
)

data class DetailResponse(

	@field:SerializedName("product")
	val product: ProductsItem? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ProductsItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("color")
	val color: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("discount")
	val discount: Int? = null,

	@field:SerializedName("model")
	val model: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("brand")
	val brand: String? = null,

	@field:SerializedName("popular")
	val popular: Boolean? = null,

	@field:SerializedName("onSale")
	val onSale: Boolean? = null
)
