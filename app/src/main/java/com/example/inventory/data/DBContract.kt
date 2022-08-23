package com.example.inventory.data

import android.provider.BaseColumns

object DBContract {

    /* Inner class that defines the table contents */
    class ItemEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "voyages"
            const val COLUMN_CODE = "code"
            const val COLUMN_DEPART = "depart"
            const val COLUMN_DESTINATION = "destination"
            const val COLUMN_TRANSPORTEUR = "transporteur"
            
        }
    }
}