package com.example.inventory

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.inventory.data.Item
import com.example.inventory.databinding.ItemListFragmentBinding
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.NonCancellable.start
import java.text.NumberFormat

class TotalFragment(val allItems: List<Item>) : DialogFragment() {

   var total: Double = 0.0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        for (item in allItems){
            total += item.itemPrice.toDouble()*item.quantityInStock.toDouble()
        }


        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

            builder.setMessage("Le Total est ${NumberFormat.getCurrencyInstance().format(total)}\$")
                .setNeutralButton("OK" ,DialogInterface.OnClickListener { dialog, id ->
                // START THE GAME!
            })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

