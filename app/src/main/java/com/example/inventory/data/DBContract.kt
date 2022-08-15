package com.example.inventory.data

import android.provider.BaseColumns

object DBContract {

    /* Inner class that defines the table contents */
    class ItemEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "items"
            val COLUMN_ID = "id"
            val COLUMN_NAME = "name"
            val COLUMN_CATEGORY = "category"
            val COLUMN_PRICE = "price"
            val COLUMN_QUANTITY = "quantity"
        }
    }
}