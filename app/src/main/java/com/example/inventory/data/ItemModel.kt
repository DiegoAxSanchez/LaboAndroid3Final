package com.example.inventory.data

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.text.NumberFormat

class ItemModel(
    val id: Int?,
    val name: String,
    val category: String,
    val price : Double,
    val quantity: Int,
    )

    fun isEntryValid(
        itemName: String,
        itemCategory: String,
        itemPrice: String,
        itemCount: String
    ): Boolean {
        if (itemName.isBlank() || itemCategory.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

fun getFormattedPrice(itemPrice: Double): String {
    return NumberFormat.getCurrencyInstance().format(itemPrice)
}

