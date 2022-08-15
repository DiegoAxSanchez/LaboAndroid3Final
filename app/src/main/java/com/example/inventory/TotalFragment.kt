package com.example.inventory

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.inventory.data.ItemModel
import java.text.NumberFormat

class TotalFragment(private val allItems: List<ItemModel>) : DialogFragment() {

   private var total: Double = 0.0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        for (item in allItems){
            total += item.price *item.quantity.toDouble()
        }


        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

            builder.setMessage("Le Total est ${NumberFormat.getCurrencyInstance().format(total)}\$")
                .setNeutralButton("OK" , { dialog, id ->
                // START THE GAME!
            })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

